package org.certeasy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

import org.junit.jupiter.api.Assertions;

import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.Month;
import java.util.Base64;
import java.util.Set;

public enum StoredCert {

    ROOT_CA("root-ca",
            new CertAttributes(
                    "711693078e7715874190cc27d222d67713243974",
                    DistinguishedName.builder().parse("CN=example-root, C=MZ, O=Example Inc").build(),
                    null,
                    DistinguishedName.builder().parse("CN=example-root, C=MZ, O=Example Inc").build(),
                    KeyStrength.HIGH,new DateRange(LocalDate.of(2023, Month.JUNE, 12), LocalDate.of(2050, Month.OCTOBER, 28)),
                    new BasicConstraints(true),
                    Set.of(KeyUsage.DigitalSignature, KeyUsage.SignCRL), null
            )
    ),

    INTERMEDIATE_CA("intermediate-ca",
            new CertAttributes(
                    "0f605b5a26a9b51108277a0a4eed816dcfccdcbf",
                    DistinguishedName.builder().parse("CN=example-intermediate, C=MZ, O=Example Inc").build(),
                    null,
                    DistinguishedName.builder().parse("CN=example-root, C=MZ, O=Example Inc").build(),
                    KeyStrength.HIGH,new DateRange(LocalDate.of(2023, Month.JUNE, 12), LocalDate.of(2050, Month.OCTOBER, 28)),
                    new BasicConstraints(true, 5),
                    Set.of(KeyUsage.DigitalSignature, KeyUsage.SignCRL), null
            )
    ),

    WWW("www",
            new CertAttributes(
                    "09a5debe03bbe41a371a2f7aa5119c71327fd178",
                    DistinguishedName.builder().parse("CN=www.example.com, C=MZ, O=Example Inc").build(),
                    Set.of(new SubjectAlternativeName(SubjectAlternativeNameType.DNS,"www.example.com"), new SubjectAlternativeName(SubjectAlternativeNameType.DNS,"smptp.example.com")),
                    DistinguishedName.builder().parse("CN=example-intermediate, C=MZ, O=Example Inc").build(),
                    KeyStrength.HIGH,new DateRange(LocalDate.of(2023, Month.JUNE, 12), LocalDate.of(2050, Month.OCTOBER, 28)),
                    new BasicConstraints(false),
                    Set.of(KeyUsage.DigitalSignature, KeyUsage.SignCRL), null
            )
    );

    private Path directory;
    private byte[] derBytes;
    private PrivateKey privateKey;

    private CertAttributes attributes;

    private StoredCert(String name, CertAttributes attributes){
        this.directory = Path.of("src/test/resources/certs",name);
        this.attributes = attributes;
    }

    public void load(){
        if(derBytes==null)
            this.loadDerBytes();
        if(privateKey==null)
            this.loadPrivateKey();
    }

    private void loadDerBytes(){
        try (BufferedReader reader = new BufferedReader(new FileReader(directory.resolve("certificate.crt").toFile()))) {
            StringBuilder certBuilder = new StringBuilder();
            String line;
            // Read the PEM file contents
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("-----BEGIN CERTIFICATE-----") || line.startsWith("-----END CERTIFICATE-----")) {
                    continue;
                }
                certBuilder.append(line.trim());
            }
            // Base64 decode the key
            this.derBytes = Base64.getDecoder().decode(certBuilder.toString());
        } catch (Exception ex) {
            Assertions.fail("error loading der bytes", ex);
        }
    }

    private void loadPrivateKey(){
        try (BufferedReader reader = new BufferedReader(new FileReader(directory.resolve("private.key").toFile()))) {
            StringBuilder keyBuilder = new StringBuilder();
            String line;
            // Read the PEM file contents
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("-----BEGIN PRIVATE KEY-----") || line.startsWith("-----END PRIVATE KEY-----")) {
                    continue;
                }
                keyBuilder.append(line.trim());
            }
            // Base64 decode the key
            byte[] keyBytes = Base64.getDecoder().decode(keyBuilder.toString());
            // Generate the PKCS8EncodedKeySpec
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            // Create an RSA KeyFactory
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // Generate the PrivateKey
            this.privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            Assertions.fail("error loading private key", ex);
        }
    }

    public byte[] getDerBytes() {
        if(derBytes==null)
            this.loadDerBytes();
        return derBytes;
    }

    public PrivateKey getPrivateKey() {
        if(privateKey==null)
            this.loadPrivateKey();
        return privateKey;
    }

    public CertAttributes getAttributes() {
        return attributes;
    }

    public TestCert asTestCert(Certificate certificate) {
        return new TestCert(certificate,  getDerBytes(), getPrivateKey(), attributes);
    }

}
