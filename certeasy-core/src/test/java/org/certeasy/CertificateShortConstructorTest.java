package org.certeasy;

public class CertificateShortConstructorTest implements CertificateBaseTest {

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
        CertificateSpec spec = new CertificateSpec(new CertificateSubject(attributes.subject()), attributes.keyStrength() , attributes.validity(), attributes.basicConstraints(), attributes.keyUsages(), attributes.extendedKeyUsages());
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



    /*
    - constructor must require non-null serial
    - constructor must require non-null derBytes
    - constructor must require non-null privateKey
    - constructor must require non-null spec
    - constructor must require non-null issuerDistinguishedName


    - getKeyStrength() must return the KeyStrength value from spec
    - getValidityPeriod() must return validity from spec
    - getDistinguishedName() must return DistinguishedName from spec
    - getExtendedKeyUsages() must return non-empty optional when spec has KeyUsages
    - getExtendedKeyUsages() must return empty optional when spec has no KeyUsages
    - getSubjectAltNames() must return empty set if spec has no SANs set
    - getSubjectAltNames() must return non-empty if spec has SANs set
    - getBasicConstraints() must return basic constraint from spec
    - isCA() must return false if spec basic constraints ca attribute is false
    - isCA() must return true if spec basic constraints ca attribute is true

     */

}
