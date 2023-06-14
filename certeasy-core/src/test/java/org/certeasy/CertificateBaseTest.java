package org.certeasy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

interface CertificateBaseTest {

    TestCert getRootCACert();

    TestCert getSubCACert();

    TestCert getSiteCert();



    @Test
    @DisplayName("getSerial() must return serial passed on constructor")
    default void getSerial_must_return_serial_passed_on_constructor(){
        TestCert cert = getSiteCert();
        assertEquals(cert.attributes().serial(), cert.certificate().getSerial());
    }

    @Test
    @DisplayName("getPrivateKey() must return private key supplied on constructor")
    default void getPrivateKey_must_return_private_key_supplied_on_constructor(){
        TestCert cert = getSiteCert();
        assertEquals(cert.privateKey(), cert.certificate().getPrivateKey());
    }


    @Test
    @DisplayName("getDERBytes() must return der bytes supplied on constructor")
    default void getDERBytes_must_return_der_bytes_supplied_on_constructor(){
        TestCert cert = getSiteCert();
        assertEquals(cert.derBytes(), cert.certificate().getDERBytes());
    }

    @Test
    @DisplayName("exportDER() must require non-null OutputStream")
    default void exportDER_must_require_non_null_outputStream(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert testCert = getSiteCert();
            Certificate certificate = testCert.certificate();
            certificate.exportDER( (OutputStream) null);
        });
    }


    @Test
    @DisplayName("exportDER() must require non-null file")
    default void exportDER_must_require_non_null_file(){
        assertThrows(IllegalArgumentException.class, () -> {
            TestCert testCert = getSiteCert();
            Certificate certificate = testCert.certificate();
            certificate.exportDER( (File) null);
        });
    }

    @Test
    @DisplayName("exportDER() must fail if file is pointing to directory")
    default void exportDER_must_fail_if_file_is_pointing_to_directory(){
        assertThrows(IllegalArgumentException.class, () -> {
            Certificate certificate = getSiteCert().certificate();
            File file = new File("target");
            certificate.exportDER(file);
        });
    }


    @Test
    @DisplayName("exportDER() must create file if not exists")
    default void exportDER_must_create_file_if_not_exists() throws IOException {
        Certificate certificate = getSiteCert().certificate();
        File file = File.createTempFile("certeasy",".crt");
        certificate.exportDER(file);
        assertTrue(file.exists());
        file.delete();
    }


    @Test
    @DisplayName("exportDER() must write der bytes to existing file")
    default void exportDER_must_write_der_bytes_to_existing_file() throws IOException{
        Certificate certificate = getSiteCert().certificate();
        File file = File.createTempFile("certeasy",".der");
        certificate.exportDER(file);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    @DisplayName("exportDER() must write DER bytes to outputStream")
    default void exportDER_must_write_der_bytes_to_output_stream() throws IOException {
        TestCert testCert = getSiteCert();
        Certificate certificate = testCert.certificate();
        File derFile = File.createTempFile("certeasy",".crt");
        try(OutputStream out = new java.io.FileOutputStream(derFile)) {
            certificate.exportDER(out);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
        try (FileInputStream fileInputStream = new FileInputStream(derFile)) {
            byte[] actualBytes = fileInputStream.readAllBytes();
            assertEquals(testCert.derBytes().length, actualBytes.length);
            String actualBase64 = Base64.getEncoder().encodeToString(actualBytes);
            String originalBase64 = Base64.getEncoder().encodeToString(testCert.derBytes());
            assertEquals(originalBase64, actualBase64);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    @DisplayName("getSummary() must return certificate summary")
    default void getSummary_must_return_certificate_summary(){

    }


    @Test
    @DisplayName("getIssuerName() must return certificate own DN if self-signed")
    default void getIssuerName_must_return_certificate_own_DN_if_self_signed(){
        TestCert testCert = getRootCACert();
        assertEquals(testCert.attributes().subject().toString(), testCert.certificate().getIssuerName().toString());
    }


    @Test
    @DisplayName("getIssuerName() must return value different from certificate DN if not self-signed")
    default void getIssuerName_must_return_value_different_from_certificate_DN_if_not_self_signed(){
        TestCert testCert = getSubCACert();
        assertNotEquals(testCert.attributes().subject().toString(), testCert.certificate().getIssuerName().toString());
    }



    @Test
    @DisplayName("isSelfSignedCA() must return true if issuerDistinguishedName equals certificate own DN")
    default void isSelfSignedCA_must_return_true_if_issuerDistinguishedName_equals_certificate_own_DN(){
        TestCert testCert = getRootCACert();
        assertTrue(testCert.certificate().isSelfSignedCA());
    }


    @Test
    @DisplayName("isSelfSignedCA() must return false if issuerDistinguishedName does not equal certificate DN")
    default void isSelfSignedCA_must_return_false_if_issuerDistinguishedName_does_not_equal_certificate_DN(){
        TestCert testCert = getSubCACert();
        assertFalse(testCert.certificate().isSelfSignedCA());
        testCert = getSiteCert();
        assertFalse(testCert.certificate().isSelfSignedCA());
    }


}
