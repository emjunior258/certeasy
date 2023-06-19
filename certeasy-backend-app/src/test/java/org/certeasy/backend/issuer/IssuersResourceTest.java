package org.certeasy.backend.issuer;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.certeasy.*;
import org.certeasy.backend.common.CertPEM;
import org.certeasy.backend.common.SubCaSpec;
import org.certeasy.backend.common.cert.CertValidity;
import org.certeasy.backend.common.cert.GeographicAddressInfo;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.backend.persistence.MapIssuerRegistry;
import org.certeasy.backend.persistence.MemoryPersistenceProfile;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.certeasy.certspec.PersonalCertificateSpec;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;

@QuarkusTest
@TestProfile(MemoryPersistenceProfile.class)
@TestHTTPEndpoint(IssuersResource.class)
public class IssuersResourceTest {

    @Inject
    IssuerRegistry registry;

    @Inject
    CertEasyContext context;

    private CertificateAuthoritySpec certificateAuthoritySpec;
    private PersonalCertificateSpec personalCertificateSpec;

    public IssuersResourceTest(){
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
    }

    @BeforeEach
    void clear_registry(){
        MapIssuerRegistry mapIssuerRegistry = (MapIssuerRegistry) registry;
        mapIssuerRegistry.clear();
    }


    @Test
    @DisplayName("listIssuers() must return empty array")
    void listIssuers_must_return_empty_array(){
        given().when().get()
                .then()
                .assertThat()
                    .statusCode(200)
                    .body(equalTo("[]"));

    }

    @Test
    @DisplayName("listIssuers() must return existing issuer")
    void listIssuers_must_return_existing_issuer(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        registry.add("example", authorityCert);

        IssuerInfo[] certs = given().when().get()
                .as(IssuerInfo[].class);
        assertEquals(1, certs.length);

        IssuerInfo issuerInfo = certs[0];
        assertEquals("example", issuerInfo.id());
        assertEquals(authorityCert.getSerial(), issuerInfo.serial());
        assertEquals(10, issuerInfo.pathLength());
        assertEquals("CN=Root, C=ZA, ST=Nelspruit, L=Mpumalanga, STREET=Third Base Urban. Fashion. UG73", issuerInfo.distinguishedName());

    }


    @Test
    @DisplayName("deleteIssuer() must remove existing issuer")
    void deleteIssuer_must_remove_existing_issuer(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        registry.add("dummy", authorityCert);
        assertFalse(registry.list().isEmpty());

        given().delete("/dummy")
                .then()
                .statusCode(204);

        assertTrue(registry.list().isEmpty());

    }

    @Test
    @DisplayName("createFromPem() must create issuer successfully")
    void createFromPem_must_create_issuer_successfully() throws IOException {

        assertFalse(registry.exists("ipsum"));

        Path pemDirectory = Paths.get("src/test/resources/pem/ca");
        String certPem = Files.readString(pemDirectory.resolve("cert.pem"));
        String keyPem = Files.readString(pemDirectory.resolve("key.pem"));

        CertPEM pem = new CertPEM(certPem, keyPem);
        given().contentType(ContentType.JSON)
                .body(pem)
                .when()
                    .post("/ipsum/cert-pem")
                .then()
                .statusCode(204);

        assertTrue(registry.exists("ipsum"));

    }

    @Test
    @DisplayName("createFromPem() must fail when cert is not ca")
    void createFromPem_must_fail_when_cert_is_not_ca() throws IOException {

        Path pemDirectory = Paths.get("src/test/resources/pem/personal");
        String certPem = Files.readString(pemDirectory.resolve("cert.pem"));
        String keyPem = Files.readString(pemDirectory.resolve("key.pem"));

        CertPEM pem = new CertPEM(certPem, keyPem);
        given().contentType(ContentType.JSON)
                .body(pem)
                .when()
                .post("/ipsum/cert-pem")
                .then()
                .statusCode(422)
                .body("type", equalTo("/problems/constraint-violation"))
                .body("title", equalTo("Constraint Violation"))
                .body("detail", equalTo("The request violates one or more constraints"))
                .body("status", equalTo(422))
                .body("violations", hasSize(1))
                .body("violations[0].field", equalTo("body.pem.cert_file"))
                .body("violations[0].type", equalTo("state"))
                .body("violations[0].message", equalTo("cert_file is does not have CA basic constraint"));

    }

    @Test
    @DisplayName("createFromSpec() must create issuer successfully")
    void createFromSpec_must_create_issuer_successfully() throws IOException {

        SubCaSpec spec = new SubCaSpec();
        spec.setName("apple-ca");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setPathLength(4);
        spec.setGeographicAddressInfo(new GeographicAddressInfo("US", "California", "Cupertino", "One Apple Park Way, Cupertino, CA 95014"));
        spec.setValidity(new CertValidity("2023-01-01", "2099-12-31"));

        assertFalse(registry.exists("apple-ca"));

        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/apple/cert-spec")
                .then()
                .statusCode(204)
                .log().all();

        assertTrue(registry.exists("apple"));

    }

    @Test
    @DisplayName("createFromSpec() must fail whe issuer id is taken")
    void createFromSpec_must_fail_when_issuer_id_is_taken() {

        Certificate certificate = context.generator().generate(certificateAuthoritySpec);
        registry.add("microsoft", certificate);
        
        SubCaSpec spec = new SubCaSpec();
        spec.setName("Microsoft-CA");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setPathLength(4);
        spec.setGeographicAddressInfo(new GeographicAddressInfo("US", "California", "Cupertino", "One Apple Park Way, Cupertino, CA 95014"));
        spec.setValidity(new CertValidity("2023-01-01", "2099-12-31"));

        assertFalse(registry.exists("apple-ca"));

        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/microsoft/cert-spec")
                .then()
                .statusCode(409)
                .body("type", equalTo("/problems/issuerId/id-taken"))
                .body("title", equalTo("Issuer ID Taken"))
                .body("status", equalTo(409));
    }

    @Test
    @DisplayName("createFromSpec() must fail when validity is null")
    void createFromSpec_must_fail_when_validity_is_null() {

        SubCaSpec spec = new SubCaSpec();
        spec.setName("Google");
        spec.setKeyStrength(KeyStrength.VERY_HIGH.name());
        spec.setPathLength(3);
        spec.setGeographicAddressInfo(new GeographicAddressInfo("US", "California", "Mountain View", "1600 Amphitheatre Parkway"));

        assertFalse(registry.exists("google"));
        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/google/cert-spec")
                .then()
                .statusCode(422)
                .body("type", equalTo("/problems/constraint-violation"))
                .body("title", equalTo("Constraint Violation"))
                .body("detail", equalTo("The request violates one or more constraints"))
                .body("status", equalTo(422))
                .body("violations", hasSize(1))
                .body("violations[0].field", equalTo("body.validity"))
                .body("violations[0].type", equalTo("required"))
                .body("violations[0].message", equalTo("validity is required"));

    }

    @Test
    @DisplayName("createFromSpec() must fail when geographic address is null")
    void createFromSpec_must_fail_when_geographic_address_is_null() {

        SubCaSpec spec = new SubCaSpec();
        spec.setName("Netflix");
        spec.setKeyStrength(KeyStrength.VERY_HIGH.name());
        spec.setPathLength(1);
        spec.setValidity(new CertValidity("2023-01-01", "2099-12-31"));

        assertFalse(registry.exists("netflix"));
        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/netflix/cert-spec")
                .then()
                .statusCode(422)
                .body("type", equalTo("/problems/constraint-violation"))
                .body("title", equalTo("Constraint Violation"))
                .body("detail", equalTo("The request violates one or more constraints"))
                .body("status", equalTo(422))
                .body("violations", hasSize(1))
                .body("violations[0].field", equalTo("body.address"))
                .body("violations[0].type", equalTo("required"))
                .body("violations[0].message", equalTo("address MUST not be null"));

    }


    @Test
    @DisplayName("createFromSpec() must fail when path_length is less than -1")
    void createFromSpec_must_fail_when_path_length_is_less_than_minus_1() {

        SubCaSpec spec = new SubCaSpec();
        spec.setName("Netflix");
        spec.setKeyStrength(KeyStrength.LOW.name());
        spec.setPathLength(-2);
        spec.setValidity(new CertValidity("2025-01-01", "2099-12-31"));
        spec.setGeographicAddressInfo(new GeographicAddressInfo("US", "California", "Mountain View", "1600 Amphitheatre Parkway"));

        assertFalse(registry.exists("netflix"));
        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/netflix/cert-spec")
                .then()
                .statusCode(422)
                .body("type", equalTo("/problems/constraint-violation"))
                .body("title", equalTo("Constraint Violation"))
                .body("detail", equalTo("The request violates one or more constraints"))
                .body("status", equalTo(422))
                .body("violations", hasSize(1))
                .body("violations[0].field", equalTo("body.path_length"))
                .body("violations[0].type", equalTo("range"))
                .body("violations[0].message", equalTo("path_length should be >= -1"));

    }

    @Test
    @DisplayName("createFromSpec() must fail when name is null or empty")
    void createFromSpec_must_fail_when_name_is_null_nor_empty() {

        SubCaSpec spec = new SubCaSpec();
        spec.setName("");
        spec.setKeyStrength(KeyStrength.LOW.name());
        spec.setPathLength(-1);
        spec.setValidity(new CertValidity("2020-01-01", "2099-12-31"));
        spec.setGeographicAddressInfo(new GeographicAddressInfo("US", "California", "Mountain View", "1600 Amphitheatre Parkway"));

        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/emptyname/cert-spec")
                .then()
                .statusCode(422)
                .body("type", equalTo("/problems/constraint-violation"))
                .body("title", equalTo("Constraint Violation"))
                .body("detail", equalTo("The request violates one or more constraints"))
                .body("status", equalTo(422))
                .body("violations", hasSize(1))
                .body("violations[0].field", equalTo("body.name"))
                .body("violations[0].type", equalTo("required"))
                .body("violations[0].message", equalTo("name is required"));


        spec.setName(null);

        given().contentType(ContentType.JSON)
                .body(spec)
                .when()
                .post("/nameless/cert-spec")
                .then()
                .statusCode(422)
                .body("type", equalTo("/problems/constraint-violation"))
                .body("title", equalTo("Constraint Violation"))
                .body("detail", equalTo("The request violates one or more constraints"))
                .body("status", equalTo(422))
                .body("violations", hasSize(1))
                .body("violations[0].field", equalTo("body.name"))
                .body("violations[0].type", equalTo("required"))
                .body("violations[0].message", equalTo("name is required"));

    }

}
