package org.certeasy.bouncycastle;

import org.certeasy.Certificate;
import org.certeasy.CertificateSubject;
import org.certeasy.DateRange;
import org.certeasy.GeographicAddress;
import org.certeasy.KeyStrength;
import org.certeasy.PEMCoderException;
import org.certeasy.SubjectAlternativeName;
import org.certeasy.SubjectAlternativeNameType;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class BouncyCastlePEMCoderTest {
    static BouncyCastlePEMCoder bcPEMCoder;
    static Certificate certificate;
    static String certPem;
    static String privateKeyPem;

    @BeforeAll
    static void initAll() {
        bcPEMCoder = new BouncyCastlePEMCoder();

        CertificateAuthoritySubject subject = new CertificateAuthoritySubject("John Tester",
                new GeographicAddress("MZ", "Maputo", "KaMabukwane", "Av. F"));

        CertificateSubject certSubject = new CertificateSubject(subject.getDistinguishedName(),
                Set.of(new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "jtester")));

        DateRange dateRange = new DateRange(LocalDate.now().plusDays(2));
        CertificateAuthoritySpec spec = new CertificateAuthoritySpec(subject, 0, KeyStrength.LOW, dateRange);

        certificate = new BouncyCastleCertGenerator().generate(spec);

        privateKeyPem = """
                -----BEGIN PRIVATE KEY-----
                MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA5cJL0DFnGfc7SDJb
                a7sxpJG4fBxceaOqVJ/tVOXP4rK3svA29KezQwfFIPWQTbk3oI9gOiRq9z+5oA3I
                BjWdvwIDAQABAkAUoa7nIhiNB1tmb0cwVF3v1joHwxA2yBCaisXoEr7pL1GcUZdx
                i0IA3l9+aBMMHaGV/8EcSG1D7rBoyMHrVvrBAiEA/1vezAjORie69Pnr0BtHG/lb
                lfaTVH4rOVT3ewJQnt8CIQDmVfjDZS9b9kPK8Sjru5Kz67NfQFMIRrVk9FcK2Mo9
                IQIgKd3tLHSxJS10aJ/lOsyOHxcGHhM8WpkJCzBUFxXtE+8CIQCYFyh9jPCqnn/B
                H2BP17lUyEvWl1i8XEQmdxiKdv+DQQIhAICGrpOTWuzIR6q7Q3ncdg9JgXw+Pd4x
                MqV8rkBjhrMu
                -----END PRIVATE KEY-----
                """;

        certPem = """
                -----BEGIN CERTIFICATE-----
                MIIB0DCCAXqgAwIBAgIGAYi9YuwpMA0GCSqGSIb3DQEBCwUAMFoxDzANBgNVBAgT
                Bk1hcHV0bzEUMBIGA1UEBxMLS2FNYWJ1a3dhbmUxFDASBgNVBAMTC0pvaG4gVGVz
                dGVyMQswCQYDVQQGEwJNWjEOMAwGA1UECRMFQXYuIEYwHhcNMjMwNjE0MjIwMDAw
                WhcNMjMwNjE1MjE1OTAwWjBaMQ8wDQYDVQQIEwZNYXB1dG8xFDASBgNVBAcTC0th
                TWFidWt3YW5lMRQwEgYDVQQDEwtKb2huIFRlc3RlcjELMAkGA1UEBhMCTVoxDjAM
                BgNVBAkTBUF2LiBGMFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIh6w9Vcf3nS9WlQ
                AOcsL6xC7c0/1SI7C5ZPZ9LkCJ5UJX8+3+mx9bne3djPLbOyrmbGUBN48J0eUFyi
                sJYB8vsCAwEAAaMmMCQwEgYDVR0TAQH/BAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMC
                AQYwDQYJKoZIhvcNAQELBQADQQBOEg2E0ZD77Gd8Bb8/p6zC/WhfQzzidPX8NRNp
                cvTXQYIabZXHSDbyPN0KyjreWPVilPjNftI6kv2Uzt+J5dWE
                -----END CERTIFICATE-----
                """;
    }

    @Test
    @DisplayName("must throw an exception when decode with empty or null parameters")
    void mustThrowExceptionWhenDecodeWithEmptyOrNullParameters() {

        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.decodeCertificate("", privateKeyPem),
                "certPem MUST not be null nor empty");

        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.decodeCertificate(null, ""),
                "certPem MUST not be null nor empty");


        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.decodeCertificate("certPem", ""),
                "privateKeyPem MUST not be null nor empty");

        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.decodeCertificate(certPem, ""),
                "privateKeyPem MUST not be null nor empty");

    }

    @Test
    @DisplayName("must throw an exception when decode ")
    void mustThrowExceptionWhenDecode() {

        assertThrows(PEMCoderException.class,
                () -> bcPEMCoder.decodeCertificate("certPem", "privateKeyPem"));

    }

    @Test
    @DisplayName("must Decode Certificate And PrivateKey")
    void mustDecodeCertificateAndPrivateKey() {
        Certificate decodedCertificate = bcPEMCoder.decodeCertificate(certPem, privateKeyPem);
        assertNotNull(decodedCertificate);
        assertNotEquals(certificate, decodedCertificate);
    }

    @Test
    @DisplayName("must throw exception when encode private key with null certificate")
    void mustThrowExceptionWhenEncodePrivateKeyWithNullCertificate() {
        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.encodePrivateKey(null),
                "certificate MUST not be null");
    }

    @Test
    @DisplayName("must Encode PrivateKey With Certificate")
    void mustEncodePrivateKeyWithCertificate() {
        String privateKey = bcPEMCoder.encodePrivateKey(certificate);
        assertNotNull(privateKey);
        assertNotEquals("", privateKey);
    }

    @Test
    @DisplayName("must throw exception when encode certificate with null certificate")
    void mustThrowExceptionWhenEncodeCertificateWithNullCertificate() {
        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.encodeCert(null),
                "certificate MUST not be null");
    }

    @Test
    @DisplayName("must Encode Certificate With Certificate")
    void mustEncodeCertificateWithCertificate() {
        String cert = bcPEMCoder.encodeCert(certificate);
        assertNotNull(cert);
        assertNotEquals("", cert);
    }

    @Test
    @DisplayName("must throw exception when encode Chain With Null Or Empty Chain")
    void mustThrowExceptionWhenEncodeChainWithNullOrEmptyChain() {
        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.encodeChain(null),
                "chain MUST not be null nor empty");

        assertThrows(IllegalArgumentException.class,
                () -> bcPEMCoder.encodeChain(Set.of()),
                "chain MUST not be null nor empty");
    }

    @Test
    @DisplayName("must Encode Chain With Certificate")
    void mustEncodeChainWithCertificate() {
        String chain = bcPEMCoder.encodeChain(Set.of(certificate));
        assertNotNull(chain);
        assertNotEquals("", chain);
    }

}