package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CertificateShortConstructorTest implements CertificateBaseTest {

    private TestCert rootCa;
    private TestCert intermediateCa;
    private TestCert wwwCert;

    public CertificateShortConstructorTest(){
        this.rootCa = makeTestCert(StoredCert.ROOT_CA);
        this.intermediateCa = makeTestCert(StoredCert.INTERMEDIATE_CA);
        this.wwwCert = makeTestCert(StoredCert.WWW);
    }

    private TestCert makeTestCert(StoredCert storedCert){
        CertAttributes attributes = storedCert.getAttributes();
        CertificateSpec spec = new CertificateSpec(new CertificateSubject(attributes.subject(), attributes.subjectAltNames()), attributes.keyStrength() , attributes.validity(), attributes.basicConstraints(), attributes.keyUsages(), attributes.extendedKeyUsages());
        Certificate certificate = new Certificate(spec, attributes.serial(), storedCert.getPrivateKey(), storedCert.getDerBytes(),
                attributes.issuer());
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


    private CertificateSpec makeSpec(TestCert cert){
        return makeSpec(cert, cert.attributes().extendedKeyUsages());
    }

    private CertificateSpec makeSpec(TestCert cert, ExtendedKeyUsages extendedKeyUsages){
        CertAttributes attrs = cert.attributes();
        return new CertificateSpec(new CertificateSubject(cert.certificate().getDistinguishedName(), attrs.subjectAltNames()),
                attrs.keyStrength(), attrs.validity(),
                attrs.basicConstraints(), attrs.keyUsages(),
                extendedKeyUsages);
    }

    @Test
    @DisplayName("constructor must require non-null serial")
    public void constructor_must_require_non_null_serial(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        assertThrows(IllegalArgumentException.class, () -> new Certificate(spec, null, cert.privateKey(), cert.derBytes(), attrs.issuer()));
    }

    @Test
    @DisplayName("constructor must require non-null derBytes")
    public void constructor_must_receive_non_null_derBytes(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        assertThrows(IllegalArgumentException.class, () -> new Certificate(spec, "0123456789", cert.privateKey(), null, attrs.issuer()));
    }

    @Test
    @DisplayName("constructor must require non-null privateKey")
    public void constructor_must_require_non_null_privateKey(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        assertThrows(IllegalArgumentException.class, () -> new Certificate(spec, "0123456789", null, cert.derBytes(), attrs.issuer()));
    }

    @Test
    @DisplayName("constructor must require non-null spec")
    public void constructor_must_require_non_null_spec(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        assertThrows(IllegalArgumentException.class, () -> new Certificate(null, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer()));
    }

    @Test
    @DisplayName("constructor must require non-null issuerDistinguishedName")
    public void constructor_must_require_non_null_issuerDistinguishedName(){
        TestCert cert = getSubCACert();
        CertificateSpec spec = makeSpec(cert);
        assertThrows(IllegalArgumentException.class, () -> new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), null));
    }

    @Test
    @DisplayName("getKeyStrength() must return the KeyStrength value from spec")
    public void getKeyStrength_must_return_the_KeyStrength_value_from_spec(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertEquals(attrs.keyStrength(), certificate.getKeyStrength());
    }

    @Test
    @DisplayName("getValidityPeriod() must return validity from spec")
    public void getValidityPeriod_must_return_validity_from_spec(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertEquals(attrs.validity(), certificate.getValidityPeriod());
    }

    @Test
    @DisplayName("getDistinguishedName() must return DistinguishedName from spec")
    public void getDistinguishedName_must_return_DistinguishedName_from_spec(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertEquals(attrs.subject(), certificate.getDistinguishedName());
    }

    @Test
    @DisplayName("getExtendedKeyUsages() must return non-empty optional when spec has ExtendedKeyUsages")
    public void getExtendedKeyUsages_must_return_non_empty_optional_when_spec_has_extendedKeyUsages(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert, new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION, ExtendedKeyUsage.TLS_WEB_SERVER_AUTH)));
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertFalse(certificate.getExtendedKeyUsages().isEmpty());
    }

    @Test
    @DisplayName("getExtendedKeyUsages() must return empty optional when spec has no ExtendedKeyUsages")
    public void getExtendedKeyUsages_must_return_empty_optional_when_spec_has_no_ExtendedKeyUsages(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert, null);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertTrue(certificate.getExtendedKeyUsages().isEmpty());
    }

    @Test
    @DisplayName("getSubjectAltNames() must return empty set if spec has no SANs set")
    public void getSubjectAltNames_must_return_empty_set_if_spec_has_no_SANs_set(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertTrue(certificate.getSubjectAltNames().isEmpty());
    }

    @Test
    @DisplayName("getSubjectAltNames() must return non-empty if spec has SANs set")
    public void getSubjectAltNames_must_return_non_empty_if_spec_has_SANs_set(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert, new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION, ExtendedKeyUsage.TLS_WEB_SERVER_AUTH)));
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertFalse(certificate.getSubjectAltNames().isEmpty());
    }

    @Test
    @DisplayName("getBasicConstraints() must return basic constraint from spec")
    public void getBasicConstraints_must_return_basic_constraint_from_spec(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertEquals(attrs.basicConstraints(), certificate.getBasicConstraints());
    }

    @Test
    @DisplayName("isCA() must return false if spec basic constraints ca attribute is false")
    public void isCA_must_return_false_if_spec_basic_constraints_ca_attribute_is_false(){
        TestCert cert = getSiteCert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertFalse(certificate.isCA());
    }

    @Test
    @DisplayName("isCA() must return true if spec basic constraints ca attribute is true")
    public void isCA_must_return_true_if_spec_basic_constraints_ca_attribute_is_true(){
        TestCert cert = getSubCACert();
        CertAttributes attrs = cert.attributes();
        CertificateSpec spec = makeSpec(cert);
        Certificate certificate = new Certificate(spec, "0123456789", cert.privateKey(), cert.derBytes(), attrs.issuer());
        assertTrue(certificate.isCA());
    }

}
