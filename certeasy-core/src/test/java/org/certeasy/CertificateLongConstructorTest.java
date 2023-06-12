package org.certeasy;


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
        Certificate certificate = new Certificate(attributes.serial(), attributes.subject(), attributes.validity(), attributes.keyStrength(), attributes.basicConstraints(), storedCert.getPrivateKey(), storedCert.getDerBytes(), attributes.sans(), attributes.keyUsages(), attributes.issuer(), attributes.extendedKeyUsages());
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
    - constructor must require non-null distinguishedName
    - constructor must require non-null validityPeriod
    - constructor must require non-null keyStrength
    - constructor must require non-null basicConstraints
    - constructor must require non-null privateKey
    - constructor must require non-null keyUsages
    - constructor must require non-null derBytes
    - constructor must require non-empty derBytes array
    - constructor must require non-null issuerDistinguishedName
    - constructor must allow null extendedKeyUsages
    - constructor must allow null subjectAltNames


    - getKeyStrength() must return the KeyStrength value from constructor argument
    - getValidityPeriod() must return validity from constructor argument
    - getDistinguishedName() must return DistinguishedName from constructor argument
    - getExtendedKeyUsages() must return non-empty optional when constructor argument is not null
    - getExtendedKeyUsages() must return empty optional when constructor argument is null
    - getSubjectAltNames() must return empty set if constructor argument is null
    - getSubjectAltNames() must return empty set if constructor argument is empty collection
    - getSubjectAltNames() must return non-empty if constructor argument is not null
    - getBasicConstraints() must return basic constraint from constructor argument

     */

}
