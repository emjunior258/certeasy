package org.certeasy.backend.certs;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.certeasy.*;
import org.certeasy.backend.BaseRestTest;
import org.certeasy.backend.common.CertPEM;
import org.certeasy.backend.common.SubCaSpec;
import org.certeasy.backend.common.cert.CertValidity;
import org.certeasy.backend.common.cert.GeographicAddressInfo;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.issuer.IssuersResource;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.backend.persistence.MapIssuerRegistry;
import org.certeasy.backend.persistence.MemoryPersistenceProfile;
import org.certeasy.certspec.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(MemoryPersistenceProfile.class)
class CertificatesResourceTest extends BaseRestTest {

    @Inject
    IssuerRegistry registry;

    @Inject
    CertEasyContext context;

    private CertificateAuthoritySpec certificateAuthoritySpec;
    private PersonalCertificateSpec personalCertificateSpec;

    public CertificatesResourceTest(){
        GeographicAddress geographicAddress = new GeographicAddress("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73");
        CertificateAuthoritySubject certificateAuthoritySubject = new CertificateAuthoritySubject("Root",
                geographicAddress);
        certificateAuthoritySpec = new CertificateAuthoritySpec(certificateAuthoritySubject, 10,
                KeyStrength.HIGH,
                new DateRange(LocalDate.of(2099, Month.DECEMBER,
                        31)));

        PersonalIdentitySubject personalIdentitySubject = new PersonalIdentitySubject(
                new PersonName("John", "Doe"),
                geographicAddress, "+258849901110",
                Set.of("john.doe@example.com"),
                Set.of("john.doe")
        );
        personalCertificateSpec = new PersonalCertificateSpec(personalIdentitySubject,
                KeyStrength.HIGH, new DateRange(LocalDate.of(2099, Month.DECEMBER,
                31)));
    }

    @BeforeEach
    void clear_registry(){
        MapIssuerRegistry mapIssuerRegistry = (MapIssuerRegistry) registry;
        mapIssuerRegistry.clear();
    }


    @Test
    void list_must_return_all_certs_issued_plus_the_ca(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add("example", authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        IssuedCertInfo[] certInfos = given().get("/api/issuers/example/certificates")
                .as(IssuedCertInfo[].class);

        Arrays.sort(certInfos, Collections.reverseOrder());
        assertEquals(2, certInfos.length);

        IssuedCertInfo cert0 = certInfos[0];
        assertEquals("Root", cert0.name());
        assertEquals(authorityCert.getSerial(), cert0.serial());
        assertTrue(cert0.ca());

        IssuedCertInfo cert1 = certInfos[1];
        assertEquals("John Doe", cert1.name());
        assertEquals(personCertificate.getSerial(), cert1.serial());
        assertFalse(cert1.ca());

    }




    @Test
    void list_must_return_not_found_when_issuer_is_unknown(){

        given().get("/api/issuers/ghost/certificates")
                .then()
                .body("type",equalTo("/problems/issuer/not-found"))
                .body("title",equalTo("Issuer not found"))
                .body("detail",equalTo("There no issuer with a matching ID: ghost"))
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(404);

    }

    @Test
    void must_issue_tls_cert(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add("example", authorityCert);

        given().contentType(MediaType.APPLICATION_JSON)
                .body("")
                .post("/api/issuers/example/certificates/tls-server")
                .then()
                .statusCode(500).log().all();

    }


    @Test
    void must_fail_to_issue_sub_ca_cert_for_unknown_issuer(){

        SubCaSpec spec = new SubCaSpec();
        spec.setName("dumb");
        spec.setKeyStrength(KeyStrength.MEDIUM.name());
        spec.setPathLength(-1);
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(2099, Month.DECEMBER,
                31))));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post("/api/issuers/dumb/certificates/sub-ca")
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(404);


    }

    @Test
    void must_succeed_issuing_sub_ca_cert_with_valid_spec(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        registry.add("example", authorityCert);


        SubCaSpec spec = new SubCaSpec();
        spec.setName("intermediate");
        spec.setKeyStrength(KeyStrength.MEDIUM.name());
        spec.setPathLength(-1);
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA","Nelspruit", "Mpumalanga", "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3099, Month.DECEMBER,
                31))));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post("/api/issuers/example/certificates/sub-ca")
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(200);

    }

    @Test
    void must_fail_to_issue_sub_ca_cert_with_invalid_spec(){

    }

    @Test
    void must_obtain_info_of_existing_cert_successfully(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add("example", authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        given().get("/api/issuers/example/certificates/"+personCertificate.getSerial())
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(200).log().all();

    }


    @Test
    void must_respond_with_not_found_when_deleting_unknown_cert(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        registry.add("example", authorityCert);

        given().delete("/api/issuers/example/certificates/1010101")
                .then()
                .body("type",equalTo("/problems/certificate/not-found"))
                .body("title",equalTo("Certificate not found"))
                .body("detail",equalTo("There is no certificate with matching serial: 1010101"))
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(404);

    }


    @Test
    void must_fail_to_delete_ca_certificate(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        registry.add("example", authorityCert);

        given().delete("/api/issuers/example/certificates/"+authorityCert.getSerial())
                .then()
                .body("type",equalTo("/problems/certificate/read-only"))
                .body("title",equalTo("Certificate Read-only"))
                .body("detail",equalTo("Certificate is read-only therefore cannot be deleted"))
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(409);

    }


    @Test
    void must_delete_non_ca_certificate_successfully(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add("example", authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        assertEquals(2, certIssuer.listCerts().size());
        given().delete("/api/issuers/example/certificates/"+personCertificate.getSerial())
                .then()
                .statusCode(204);
        assertEquals(1, certIssuer.listCerts().size());

    }

    @Test
    void must_obtain_pem_of_existing_cert_successfully(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add("example", authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        given().get("/api/issuers/example/certificates/"+personCertificate.getSerial()+"/pem")
                .then()
                .body("cert_file", notNullValue())
                .body("key_file", notNullValue())
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(200);

    }

    @Test
    void must_obtain_der_of_existing_cert_successfully(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add("example", authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        given().get("/api/issuers/example/certificates/"+personCertificate.getSerial()+"/der")
                .then()
                .contentType(MediaType.TEXT_PLAIN)
                .statusCode(200);

    }


}
