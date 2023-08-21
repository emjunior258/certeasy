package org.certeasy.backend.certs;


import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.certeasy.*;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.persistence.*;

import static org.junit.jupiter.api.Assertions.*;

import org.certeasy.certspec.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@QuarkusTest
public class CertIssuerTest {

    @Inject
    CertEasyContext context;

    private static final String ISSUER_NAME = "dummy-issuer";

    private CertificateAuthoritySpec certificateAuthoritySpec;

    private PersonalCertificateSpec johnDoeCertSpec;
    private CertificateAuthoritySpec subCaCertSpec;


    @InjectMock
    private IssuerRegistry registry;


    public CertIssuerTest(){
        GeographicAddress geographicAddress = new GeographicAddress("MZ",
                "Maputo",
                "Kampfumo",
                "AV. 25 de Setembro. Rua dos desportistas");
        CertificateAuthoritySubject certificateAuthoritySubject = new CertificateAuthoritySubject("Root",
                geographicAddress);
        certificateAuthoritySpec = new CertificateAuthoritySpec(certificateAuthoritySubject, -1,
                KeyStrength.HIGH,
                new DateRange(LocalDate.of(2050, Month.OCTOBER,
                        21)));


        CertificateAuthoritySubject subCaSubject = new CertificateAuthoritySubject("Sub-ca",
                geographicAddress);
        subCaCertSpec = new CertificateAuthoritySpec(subCaSubject, -1,
                KeyStrength.HIGH,
                new DateRange(LocalDate.of(2090, Month.MAY,
                        31)));

        PersonalIdentitySubject personalIdentitySubject = new PersonalIdentitySubject(new PersonName("John", "Doe"), geographicAddress, "+258849901111", Set.of("john.doe@example.com"), Set.of("john.doe"));
        johnDoeCertSpec = new PersonalCertificateSpec(personalIdentitySubject,
                KeyStrength.VERY_HIGH,
                new DateRange(LocalDate.of(2050, Month.DECEMBER,
                        31)));
    }


    @Test
    @DisplayName("create instance using short constructor")
    void create_instance_using_short_constructor(){
        IssuerDatastore datastore = new MapIssuerDatastore(context);
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer( null,  datastore, context));
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer(registry, null, context));
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer(registry, datastore, null));
        new CertIssuer(registry, datastore, context);
    }


    @Test
    @DisplayName("create instance using long constructor")
    void create_instance_using_long_constructor(){
        Certificate certificate = context.generator().generate(certificateAuthoritySpec);
        IssuerDatastore datastore = new MapIssuerDatastore(context);
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer(null, datastore, context, certificate));
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer(registry, null, context, certificate));
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer(registry, datastore, null, certificate));
        assertThrows(IllegalArgumentException.class, () -> new CertIssuer(registry, datastore, context, null));
        new CertIssuer(registry, datastore, context, certificate);
    }

    @Test
    @DisplayName("hasCertificate() must be true if certificate passed on constructor")
    void hasCertificate_must_be_true_if_certificate_passed_on_constructor(){
        Certificate certificate = context.generator().generate(certificateAuthoritySpec);
        IssuerDatastore datastore = new MapIssuerDatastore(context);
        CertIssuer issuer = new CertIssuer(registry, datastore, context, certificate);
        assertTrue(issuer.hasCertificate());
    }

    @Test
    @DisplayName("getId() must return subject id")
    void getId_must_return_subject_id(){
        Certificate certificate = context.generator().generate(certificateAuthoritySpec);
        IssuerDatastore datastore = new MapIssuerDatastore(context);
        CertIssuer issuer = new CertIssuer(registry, datastore, context, certificate);
        assertEquals(certificate.getDistinguishedName().digest(), issuer.getId());
    }

    @Test
    @DisplayName("listCerts() must return whats in the datastore")
    void listCerts_must_return_whats_in_the_datastore(){
        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        assertTrue(issuer.listCerts().isEmpty());

        Certificate certificate = context.generator().generate(certificateAuthoritySpec);
        datastore.put(certificate);

        Collection<StoredCert> storedCerts = issuer.listCerts();
        assertFalse(storedCerts.isEmpty());
        assertEquals(1, storedCerts.size());
    }

    @Test
    @DisplayName("issueCert() must throw exception when certificate spec is null")
    void issueCert_must_throw_when_certificate_spec_null(){
        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        assertThrows(IllegalArgumentException.class, () -> issuer.issueCert(null), "spec MUST not be null");
        Mockito.verify(datastore, Mockito.times(1)).getIssuerCertSerial();
    }

    @Test
    @DisplayName("issueCert() must throw exception when certificate spec is not ca")
    void issueCert_must_throw_when_certificate_spec_is_not_ca(){

        Certificate certificate = context.generator().generate(johnDoeCertSpec, context.generator().generate(certificateAuthoritySpec));
        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        CertIssuer issuer = new CertIssuer(registry, datastore, context, certificate);
        datastore.putIssuerCertSerial(certificate.getSerial());
        datastore.put(certificate);

        assertThrows(IllegalStateException.class, () -> issuer.issueCert(johnDoeCertSpec), "issuer certificate is not CA");
//        Mockito.verify(datastore, Mockito.times(1)).getIssuerCertSerial();
    }

    @Test
    @DisplayName("issueCert() must fail when issuer does not have a certificate")
    void issueCert_must_fail_when_issuer_does_not_have_certificate(){
        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        assertThrows(IllegalStateException.class, () -> issuer.issueCert(certificateAuthoritySpec));
        Mockito.verify(datastore, Mockito.times(1)).getIssuerCertSerial();
    }

    @Test
    @DisplayName("issueCert() must issue and store personal certificate")
    void issueCert_must_issue_and_store_personal_certificate(){
        Certificate certificate = context.generator().generate(certificateAuthoritySpec);
        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        datastore.putIssuerCertSerial(certificate.getSerial());
        datastore.put(certificate);

        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        Certificate personalCert = issuer.issueCert(johnDoeCertSpec);
        Mockito.verify(datastore, Mockito.times(1)).put(Mockito.eq(personalCert));
    }

    @Test
    @DisplayName("listCerts() must return all previously issued certs")
    void listCerts_must_return_all_previously_issued_certs(){

        IssuerDatastore datastore = new MapIssuerDatastore(context);

        Certificate issuerCert = context.generator().generate(certificateAuthoritySpec);
        datastore.putIssuerCertSerial(issuerCert.getSerial());
        datastore.put(issuerCert);

        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        issuer.issueCert(johnDoeCertSpec);
        issuer.issueCert(johnDoeCertSpec);

        Collection<StoredCert> certificates = issuer.listCerts();
        assertFalse(certificates.isEmpty());
        assertEquals(3, certificates.size());

    }



    @Test
    @DisplayName("listCerts() must not contain deleted certificate")
    void listCerts_must_not_contain_deleted_certificate(){
        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));

        Certificate issuerCert = context.generator().generate(certificateAuthoritySpec);
        datastore.putIssuerCertSerial(issuerCert.getSerial());
        datastore.put(issuerCert);

        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        Certificate johnDoeCert = issuer.issueCert(johnDoeCertSpec);
        issuer.issueCert(subCaCertSpec);

        Collection<StoredCert> certificates = issuer.listCerts();
        assertFalse(certificates.isEmpty());
        assertEquals(3, certificates.size());

        Optional<StoredCert> johnDoeStoredCertOptional = issuer.getIssuedCert(johnDoeCert.getSerial());
        assertTrue(johnDoeStoredCertOptional.isPresent());
        StoredCert johnDoeStoredCert = johnDoeStoredCertOptional.get();
        issuer.deleteIssuedCert(johnDoeStoredCert);

        certificates = issuer.listCerts();
        assertFalse(certificates.isEmpty());
        assertEquals(2, certificates.size());
        Mockito.verify(datastore, Mockito.times(1)).deleteCert(Mockito.eq(johnDoeCert.getSerial()));

    }

    @Test
    @DisplayName("getIssuedCert() must return empty optional when datastore return empty issuerSerial")
    void getIssuedCert_must_return_empty_optional_when_datastore_returns_empty_issuerSerial(){

        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));

        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        Optional<StoredCert> storedCertOptional = issuer.getIssuedCert("0123456789");
        assertTrue(storedCertOptional.isEmpty());
        Mockito.verify(datastore, Mockito.times(1)).getCert(Mockito.eq("0123456789"));

    }


    @Test
    @DisplayName("hasCertificate() must be true if datastore returns issuerCertSerial")
    void hasCertificate_must_be_true_if_datastore_returns_issuerCertSerial(){

        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        datastore.putIssuerCertSerial("0123456789");

        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        assertTrue(issuer.hasCertificate());
        Mockito.verify(datastore, Mockito.times(1)).getIssuerCertSerial();

    }

    @Test
    @DisplayName("hasCertificate() must be false if datastore does not have issuerCertSerial")
    void hasCertificate_must_be_false_if_datastore_does_not_have_issuerCertSerial(){

        IssuerDatastore datastore = Mockito.spy(new MapIssuerDatastore(context));
        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        assertFalse(issuer.hasCertificate());
        Mockito.verify(datastore, Mockito.times(1)).getIssuerCertSerial();

    }

    @Test
    @DisplayName("disable() must cause issuer to not issue certificates")
    void disable_must_cause_issuer_to_not_issue_certificates(){

        IssuerDatastore datastore = new MapIssuerDatastore(context);

        Certificate issuerCert = context.generator().generate(certificateAuthoritySpec);
        datastore.putIssuerCertSerial(issuerCert.getSerial());
        datastore.put(issuerCert);

        CertIssuer issuer = new CertIssuer(registry, datastore, context);
        issuer.disable();
        assertTrue(issuer.isDisabled());
        assertThrows(IllegalStateException.class,() -> issuer.issueCert(johnDoeCertSpec));

    }


}
