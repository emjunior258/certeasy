package org.certeasy.backend.certs;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.certeasy.*;
import org.certeasy.backend.BaseRestTest;
import org.certeasy.backend.common.SubCaSpec;
import org.certeasy.backend.common.cert.CertValidity;
import org.certeasy.backend.common.cert.GeographicAddressInfo;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.backend.persistence.MapIssuerRegistry;
import org.certeasy.backend.persistence.MemoryPersistenceProfile;
import org.certeasy.backend.persistence.StoredCert;
import org.certeasy.certspec.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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
        CertIssuer certIssuer = registry.add(authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        CertificateSummaryInfo[] certInfos = given()
                .get(String.format("/api/issuers/%s/certificates",
                        certIssuer.getId())).as(CertificateSummaryInfo[].class);

        Arrays.sort(certInfos, Collections.reverseOrder());
        assertEquals(2, certInfos.length);

        CertificateSummaryInfo cert0 = certInfos[0];
        assertEquals("Root", cert0.name());
        assertEquals(authorityCert.getSerial(), cert0.serial());
        assertEquals(IssuedCertType.CA, cert0.type());

        CertificateSummaryInfo cert1 = certInfos[1];
        assertEquals("John Doe", cert1.name());
        assertEquals(personCertificate.getSerial(), cert1.serial());
        assertEquals(IssuedCertType.PERSONAL, cert1.type());

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
    void must_fail_to_issue_tls_cert_with_empty_spec(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        given().contentType(MediaType.APPLICATION_JSON)
                .body("")
                .post(String.format("/api/issuers/%s/certificates/tls-server", certIssuer.getId()))
                .then()
                .statusCode(500).log().all();

    }

    @Test
    void must_succeed_issuing_tls_cert_with_valid_spec(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        ServerSpec spec = new ServerSpec();
        spec.setName("certeasy.org");
        spec.setDomains(Set.of("www.certeasy.org", "certeasy.org"));
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));
        spec.setOrganization("Certeasy Inc");


        given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/tls-server", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .log().all();

    }

    @Test
    void must_succeed_issuing_tls_cert_with_spec_without_organization(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        ServerSpec spec = new ServerSpec();
        spec.setName("example.org");
        spec.setDomains(Set.of("www.example.org", "example.org"));
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));


        given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/tls-server", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .log().all();

    }

    @Test
    void must_succeed_issuing_basic_personal_cert(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        PersonalCertSpec spec = new PersonalCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));


        given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/personal", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .log().all();

    }

    @Test
    void must_succeed_issuing_employee_cert_with_all_attributes(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setOrganizationName("Example Inc");
        employmentInfo.setJobTitle("Chief Executive Dummy");
        employmentInfo.setDepartment("Executive department");
        employmentInfo.setUsername("john.doe");
        employmentInfo.setEmailAddress("john.doe@example.com");

        EmployeeCertSpec spec = new EmployeeCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmployment(employmentInfo);
        spec.setTelephone("+258841010800");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/employee", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.EMPLOYEE, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals(employmentInfo.getJobTitle(), distinguishedName.findFirst(SubjectAttributeType.TITLE).orElseThrow().value());
        assertEquals(employmentInfo.getDepartment(), distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_UNIT).orElseThrow().value());
        assertEquals(employmentInfo.getOrganizationName(), distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_NAME).orElseThrow().value());
        assertEquals(employmentInfo.getJobTitle(), distinguishedName.findFirst(SubjectAttributeType.TITLE).orElseThrow().value());
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());
        assertEquals("+258841010800", distinguishedName.findFirst(SubjectAttributeType.TELEPHONE_NUMBER).orElseThrow().value());

        Set<SubjectAlternativeName> sans = certificate.getSubjectAltNames();
        assertEquals(2, sans.size());

        Set<String> emails = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.EMAIL)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(emails.contains("john.doe@example.com"));

        Set<String> usernames = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.OTHER_NAME)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(usernames.contains("john.doe"));
    }

    @Test
    void must_succeed_issuing_employee_cert_without_department_attributes(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setOrganizationName("Example Inc");
        employmentInfo.setJobTitle("Chief Executive Dummy");
        employmentInfo.setUsername("john.doe");
        employmentInfo.setEmailAddress("john.doe@example.com");

        EmployeeCertSpec spec = new EmployeeCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmployment(employmentInfo);
        spec.setTelephone("+258841010800");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/employee", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.EMPLOYEE, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals(employmentInfo.getJobTitle(), distinguishedName.findFirst(SubjectAttributeType.TITLE).orElseThrow().value());
        assertTrue(distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_UNIT).isEmpty());
        assertEquals(employmentInfo.getOrganizationName(), distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_NAME).orElseThrow().value());
        assertEquals(employmentInfo.getJobTitle(), distinguishedName.findFirst(SubjectAttributeType.TITLE).orElseThrow().value());
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());
        assertEquals("+258841010800", distinguishedName.findFirst(SubjectAttributeType.TELEPHONE_NUMBER).orElseThrow().value());

        Set<SubjectAlternativeName> sans = certificate.getSubjectAltNames();
        assertEquals(2, sans.size());

        Set<String> emails = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.EMAIL)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(emails.contains("john.doe@example.com"));

        Set<String> usernames = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.OTHER_NAME)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(usernames.contains("john.doe"));
    }

    @Test
    void must_fail_issuing_employee_cert_with_null_employment_info(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        EmploymentInfo employmentInfo = new EmploymentInfo();

        EmployeeCertSpec spec = new EmployeeCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmployment(employmentInfo);
        spec.setTelephone("+258841010800");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        ConstraintViolationProblem problem = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/employee", certIssuer.getId()))
                .then()
                .statusCode(422)
                .extract().body().as(ConstraintViolationProblem.class);


        Set<Violation> violations = problem.getViolations();
        assertEquals(2, violations.size());

        Violation organizationNameViolation = violations.stream().filter(it -> it.field().equals("body.employment.organization_name")).findAny().orElseThrow();
        assertEquals("must specify value for organization_name", organizationNameViolation.message());
        Violation jobTitleViolation = violations.stream().filter(it -> it.field().equals("body.employment.job_title")).findAny().orElseThrow();
        assertEquals("must specify value for job_title", jobTitleViolation.message());
    }

    @Test
    void must_fail_issuing_employee_cert_with_empty_employment_info(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setOrganizationName("");
        employmentInfo.setJobTitle("");
        employmentInfo.setEmailAddress("");
        employmentInfo.setUsername("");

        EmployeeCertSpec spec = new EmployeeCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmployment(employmentInfo);
        spec.setTelephone("+258841010800");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        ConstraintViolationProblem problem = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/employee", certIssuer.getId()))
                .then()
                .statusCode(422)
                .extract().body().as(ConstraintViolationProblem.class);


        Set<Violation> violations = problem.getViolations();
        assertEquals(4, violations.size());

        Violation organizationNameViolation = violations.stream().filter(it -> it.field().equals("body.employment.organization_name")).findAny().orElseThrow();
        assertEquals("organization_name must have length greater than 0", organizationNameViolation.message());
        Violation jobTitleViolation = violations.stream().filter(it -> it.field().equals("body.employment.job_title")).findAny().orElseThrow();
        assertEquals("job_title must have length greater than 0", jobTitleViolation.message());
        Violation emailAddresseViolation = violations.stream().filter(it -> it.field().equals("body.employment.email_address")).findAny().orElseThrow();
        assertEquals("email_address must have length greater than 0", emailAddresseViolation.message());
        Violation usernameViolation = violations.stream().filter(it -> it.field().equals("body.employment.username")).findAny().orElseThrow();
        assertEquals("username must have length greater than 0", usernameViolation.message());

    }

    @Test
    void must_succeed_issuing_employee_cert_without_email_and_usernames(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setOrganizationName("Example Inc");
        employmentInfo.setJobTitle("Chief Executive Dummy");

        EmployeeCertSpec spec = new EmployeeCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmployment(employmentInfo);
        spec.setTelephone("+258841010800");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/employee", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.EMPLOYEE, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals(employmentInfo.getJobTitle(), distinguishedName.findFirst(SubjectAttributeType.TITLE).orElseThrow().value());
        assertTrue(distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_UNIT).isEmpty());
        assertEquals(employmentInfo.getOrganizationName(), distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_NAME).orElseThrow().value());
        assertEquals(employmentInfo.getJobTitle(), distinguishedName.findFirst(SubjectAttributeType.TITLE).orElseThrow().value());
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());
        assertEquals("+258841010800", distinguishedName.findFirst(SubjectAttributeType.TELEPHONE_NUMBER).orElseThrow().value());

        Set<SubjectAlternativeName> sans = certificate.getSubjectAltNames();
        assertEquals(0, sans.size());

    }

    @Test
    void must_succeed_issuing_personal_cert_with_all_attributes(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        PersonalCertSpec spec = new PersonalCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmailAddresses(Set.of("john.doe@gmail.com", "johndoe@example.com"));
        spec.setUsernames(Set.of("john.doe", "johndoe"));
        spec.setTelephone("+258841010800");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/personal", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.PERSONAL, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());
        assertEquals("+258841010800", distinguishedName.findFirst(SubjectAttributeType.TELEPHONE_NUMBER).orElseThrow().value());

        Set<SubjectAlternativeName> sans = certificate.getSubjectAltNames();
        assertEquals(4, sans.size());

        Set<String> emails = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.EMAIL)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(emails.contains("john.doe@gmail.com"));
        assertTrue(emails.contains("johndoe@example.com"));

        Set<String> usernames = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.OTHER_NAME)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(usernames.contains("john.doe"));
        assertTrue(usernames.contains("johndoe"));
    }

    @Test
    void must_succeed_issuing_personal_cert_with_emails(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        PersonalCertSpec spec = new PersonalCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setEmailAddresses(Set.of("john.doe@gmail.com", "johndoe@example.com"));
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/personal", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.PERSONAL, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());

        Set<SubjectAlternativeName> sans = certificate.getSubjectAltNames();
        assertEquals(2, sans.size());

        Set<String> usernames = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.EMAIL)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(usernames.contains("john.doe@gmail.com"));
        assertTrue(usernames.contains("johndoe@example.com"));
    }

    @Test
    void must_succeed_issuing_personal_cert_with_usernames(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        PersonalCertSpec spec = new PersonalCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setUsernames(Set.of("john.doe", "johndoe123"));
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/personal", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.PERSONAL, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());

        Set<SubjectAlternativeName> sans = certificate.getSubjectAltNames();
        assertEquals(2, sans.size());

        Set<String> usernames = sans.stream().filter(it -> it.type() == SubjectAlternativeNameType.OTHER_NAME)
                .map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(usernames.contains("john.doe"));
        assertTrue(usernames.contains("johndoe123"));
    }

    @Test
    void must_succeed_issuing_personal_cert_with_telephone(){
        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        PersonalCertSpec spec = new PersonalCertSpec();
        spec.setName("John");
        spec.setSurname("Doe");
        spec.setTelephone("+258840101023");
        spec.setKeyStrength(KeyStrength.HIGH.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("ZA",
                "Nelspruit",
                "Mpumalanga",
                "Third Base Urban. Fashion. UG73"));
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(3010,
                Month.DECEMBER, 31))));

        String serial = given().contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post(String.format("/api/issuers/%s/certificates/personal", certIssuer.getId()))
                .then()
                .statusCode(200)
                .body("serial", notNullValue())
                .extract().body().path("serial");

        StoredCert issuedCert = certIssuer.getIssuedCert(serial).orElseThrow();
        assertEquals(IssuedCertType.PERSONAL, issuedCert.getCertType());
        Certificate certificate = issuedCert.getCertificate();
        DistinguishedName distinguishedName = certificate.getDistinguishedName();

        String commonName = distinguishedName.getCommonName();
        assertEquals("John Doe", commonName);
        assertEquals("John", distinguishedName.findFirst(SubjectAttributeType.GIVEN_NAME).orElseThrow().value());
        assertEquals("Doe", distinguishedName.findFirst(SubjectAttributeType.SURNAME).orElseThrow().value());
        assertEquals("ZA", distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME).orElseThrow().value());
        assertEquals("Nelspruit", distinguishedName.findFirst(SubjectAttributeType.PROVINCE).orElseThrow().value());
        assertEquals("Mpumalanga", distinguishedName.findFirst(SubjectAttributeType.LOCALITY).orElseThrow().value());
        assertEquals("Third Base Urban. Fashion. UG73", distinguishedName.findFirst(SubjectAttributeType.STREET).orElseThrow().value());

        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.TELEPHONE_NUMBER));
        Set<RelativeDistinguishedName> telephones = distinguishedName.findAll(SubjectAttributeType.TELEPHONE_NUMBER);
        assertEquals(1, telephones.size());
        assertEquals("+258840101023", telephones.iterator().next().value());

    }

    @Test
    void must_fail_to_issue_sub_ca_cert_for_unknown_issuer(){

        SubCaSpec spec = new SubCaSpec();
        spec.setName("dumb");
        spec.setKeyStrength(KeyStrength.MEDIUM.name());
        spec.setGeographicAddressInfo(new GeographicAddressInfo("MZ", "Maputo",
                "Kampfumo", "123 BCC Devel"));
        spec.setPathLength(-1);
        spec.setValidity(new CertValidity(new DateRange(LocalDate.of(2099, Month.DECEMBER,
                31))));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(spec)
                .post("/api/issuers/010234567000010/certificates/sub-ca")
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(404);


    }

    @Test
    void must_succeed_issuing_sub_ca_cert_with_valid_spec(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer issuer = registry.add(authorityCert);

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
                .post(String.format("/api/issuers/%s/certificates/sub-ca", issuer.getId()))
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
        CertIssuer certIssuer = registry.add(authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        given().get( String.format("/api/issuers/%s/certificates/%s", certIssuer.getId(), personCertificate.getSerial()))
                .then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(200).log().all();

    }


    @Test
    void must_respond_with_not_found_when_deleting_unknown_cert(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);

        given().delete(String.format("/api/issuers/%s/certificates/1010101", certIssuer.getId()))
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
        CertIssuer certIssuer = registry.add(authorityCert);

        given().delete( String.format("/api/issuers/%s/certificates/%s",certIssuer.getId(),  authorityCert.getSerial()))
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
        CertIssuer certIssuer = registry.add(authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        assertEquals(2, certIssuer.listCerts().size());
        given().delete(String.format( "/api/issuers/%s/certificates/%s", certIssuer.getId(), personCertificate.getSerial()))
                .then()
                .statusCode(204);
        assertEquals(1, certIssuer.listCerts().size());

    }

    @Test
    void must_obtain_pem_of_existing_cert_successfully(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        given().get(String.format("/api/issuers/%s/certificates/%s/pem", certIssuer.getId(), personCertificate.getSerial()))
                .then()
                .body("cert_file", notNullValue())
                .body("key_file", notNullValue())
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(200);

    }

    @Test
    void must_obtain_der_of_existing_cert_successfully(){

        Certificate authorityCert = context.generator().generate(certificateAuthoritySpec);
        CertIssuer certIssuer = registry.add(authorityCert);
        Certificate personCertificate = certIssuer.issueCert(personalCertificateSpec);

        given().get( String.format("/api/issuers/%s/certificates/%s/der",certIssuer.getId(), personCertificate.getSerial()))
                .then()
                .contentType(MediaType.TEXT_PLAIN)
                .statusCode(200);

    }


}
