package org.certeasy.backend.issuer;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.certeasy.*;
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
}
