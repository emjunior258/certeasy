package org.certeasy;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateLongConstructorTest implements CertificateBaseTest {

    private TestCert rootCa;
    private TestCert intermediateCa;
    private TestCert wwwCert;

    public CertificateLongConstructorTest(){
        this.rootCa = makeTestCert(StoredCert.ROOT_CA);
        this.intermediateCa = makeTestCert(StoredCert.INTERMEDIATE_CA);
        this.wwwCert = makeTestCert(StoredCert.WWW);
    }

    private TestCert makeTestCert(StoredCert storedCert){
        CertAttributes attributes = storedCert.getAttributes();
        Certificate certificate = new Certificate(attributes.serial(), attributes.subject(), attributes.validity(), attributes.keyStrength(), attributes.basicConstraints(), storedCert.getPrivateKey(), storedCert.getDerBytes(), attributes.subjectAltNames(), attributes.keyUsages(), attributes.issuer(), attributes.extendedKeyUsages());
        return storedCert.asTestCert(certificate);
    }

    @Override
    public TestCert getRootCACert() {
        return this.rootCa;
    }

    @Override
    public TestCert getSubCACert() {
        return this.intermediateCa;
    }

    @Override
    public TestCert getSiteCert() {
        return this.wwwCert;
    }



    @Test
    @DisplayName("constructor must require non-null serial")
    public void constructor_must_require_non_null_serial(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate(null,
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-null distinguishedName")
    public void constructor_must_require_non_null_distinguished_name(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    null,
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }

    @Test
    @DisplayName("constructor must require non-null validityPeriod")
    public void constructor_must_require_non_null_validity_period(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    null,
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-null keyStrength")
    public void constructor_must_require_non_null_key_strength(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    null,
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-null basicConstraints")
    public void constructor_must_require_non_null_basic_constraints(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    null,
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-null privateKey")
    public void constructor_must_require_non_null_private_key(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    null,
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-null key usages")
    public void constructor_must_require_non_null_key_usages(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    null,
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }

    @Test
    @DisplayName("constructor must require non-null der-bytes")
    public void constructor_must_require_non_null_der_bytes(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    null,
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-empty der-bytes")
    public void constructor_must_require_non_empty_der_bytes(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    new byte[]{},
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    attrs.issuer(),
                    attrs.extendedKeyUsages());
        });
    }


    @Test
    @DisplayName("constructor must require non-null issuer distinguished name")
    public void constructor_must_require_non_null_issuer_distinguished_name(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert cert = getSiteCert();
            CertAttributes attrs = cert.attributes();
            new Certificate("1234567890",
                    attrs.subject(),
                    attrs.validity(),
                    attrs.keyStrength(),
                    attrs.basicConstraints(),
                    cert.privateKey(),
                    cert.derBytes(),
                    attrs.subjectAltNames(),
                    attrs.keyUsages(),
                    null,
                    attrs.extendedKeyUsages());
        });
    }

    @Test
    @DisplayName("constructor must allow null extended key usages")
    public void constructor_must_allow_null_extended_key_usages(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                attrs.subjectAltNames(),
                attrs.keyUsages(),
                attrs.issuer(),
                null);
    }


    @Test
    @DisplayName("constructor must allow null subject alt names")
    public void constructor_must_allow_null_subject_alt_names(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                null,
                attrs.keyUsages(),
                attrs.issuer(),
                attrs.extendedKeyUsages());
    }

    @Test
    @DisplayName("getKeyStrength() must return the KeyStrength value from constructor argument")
    public void getKeyStrength_must_return_the_KeyStrength_value_from_constructor_argument(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        assertEquals(attrs.keyStrength(), cert.certificate().getKeyStrength());
    }

    @Test
    @DisplayName("getValidityPeriod() must return validity from constructor argument")
    public void getValidityPeriod_must_return_validity_from_constructor_argument(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        assertEquals(attrs.validity(), cert.certificate().getValidityPeriod());
    }

    @Test
    @DisplayName("getDistinguishedName() must return DistinguishedName from constructor argument")
    public void getDistinguishedName_must_return_DistinguishedName_from_constructor_argument(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        assertEquals(attrs.subject(), cert.certificate().getDistinguishedName());
    }

    @Test
    @DisplayName("getExtendedKeyUsages() must return non-empty optional when constructor argument is not null")
    public void getExtendedKeyUsages_must_return_non_empty_optional_when_constructor_argument_is_not_null(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        Certificate certificate = new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                null,
                attrs.keyUsages(),
                attrs.issuer(),
                new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.SIGN_CODE, ExtendedKeyUsage.TLS_WEB_CLIENT_AUTH)));
        assertFalse(certificate.getExtendedKeyUsages().isEmpty());
    }

    @Test
    @DisplayName("getExtendedKeyUsages() must return empty optional when constructor argument is null")
    public void getExtendedKeyUsages_must_return_empty_optional_when_constructor_argument_is_null(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        Certificate certificate = new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                null,
                attrs.keyUsages(),
                attrs.issuer(),
                null);
        assertTrue(certificate.getExtendedKeyUsages().isEmpty());
    }

    @Test
    @DisplayName("getSubjectAltNames() must return empty set if constructor argument is null")
    public void getSubjectAltNames_must_return_empty_set_if_constructor_argument_is_null(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        Certificate certificate = new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                null,
                attrs.keyUsages(),
                attrs.issuer(),
                null);
        assertTrue(certificate.getSubjectAltNames().isEmpty());
    }

    @Test
    @DisplayName("getSubjectAltNames() must return empty set if constructor argument is empty collection")
    public void getSubjectAltNames_must_return_empty_set_if_constructor_argument_is_empty_collection(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        Certificate certificate = new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                Collections.emptySet(),
                attrs.keyUsages(),
                attrs.issuer(),
                null);
        assertTrue(certificate.getSubjectAltNames().isEmpty());
    }

    @Test
    @DisplayName("getSubjectAltNames() must return non-empty if constructor argument is not null")
    public void getSubjectAltNames_must_return_non_empty_if_constructor_argument_is_not_null(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        Certificate certificate = new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                Set.of(new SubjectAlternativeName(SubjectAlternativeNameType.DNS,
                        "example.com")),
                attrs.keyUsages(),
                attrs.issuer(),
                null);
        assertFalse(certificate.getSubjectAltNames().isEmpty());
    }

    @Test
    @DisplayName("getBasicConstraints() must return basic constraint from constructor argument")
    public void getBasicConstraints_must_return_basic_constraint_from_constructor_argument(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        Certificate certificate = new Certificate("1234567890",
                attrs.subject(),
                attrs.validity(),
                attrs.keyStrength(),
                attrs.basicConstraints(),
                cert.privateKey(),
                cert.derBytes(),
                Set.of(new SubjectAlternativeName(SubjectAlternativeNameType.DNS,
                        "example.com")),
                attrs.keyUsages(),
                attrs.issuer(),
                null);
        assertEquals(attrs.basicConstraints(), certificate.getBasicConstraints());
    }

}
