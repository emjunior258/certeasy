package org.certeasy.backend.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.certeasy.*;
import org.certeasy.backend.CertConstants;
import org.certeasy.backend.persistence.directory.DirectoryIssuerDatastore;
import org.certeasy.backend.persistence.directory.DirectoryStoredCert;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;



@QuarkusTest
@TestProfile(DirectoryPersistenceProfile.class)
public class DirectoryIssuerDatastoreTest {

    private static final Path CERTS_DIRECTORY = Path.of("target","test","certificates");

    @Inject
    CertEasyContext context;

    private CertificateAuthoritySpec certSpec;

    public DirectoryIssuerDatastoreTest(){
        GeographicAddress geographicAddress = new GeographicAddress("US",
                "Lorem",
                "Lorem Ipsum",
                "Donec blandit tortor pellentesque");
        CertificateAuthoritySubject certificateAuthoritySubject = new CertificateAuthoritySubject("Root",
                geographicAddress);
        certSpec = new CertificateAuthoritySpec(certificateAuthoritySubject, -1,
                KeyStrength.HIGH,
                new DateRange(LocalDate.of(2090, Month.DECEMBER,
                        31)));
    }

    @BeforeEach
    void clear_certificates_directory(){
        try(Stream<Path> fileStream= Files.list(CERTS_DIRECTORY)) {
            fileStream.forEach(DirectoryIssuerDatastoreTest::deleteRecursive);
        }catch (IOException ex){
            fail("Failed to cleanup test directory", ex);
        }
    }
    private static void deleteRecursive(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (Stream<Path> stream = Files.list(path)) {
                    stream.forEach(DirectoryIssuerDatastoreTest::deleteRecursive);
                }
            }
            Files.deleteIfExists(path);
            System.out.println("Deleted: "+path.toAbsolutePath().toString());
        }catch (IOException ex){
            fail("error deleting: "+path.toAbsolutePath().toString(), ex);
        }
    }

    @Test
    @DisplayName("put() must write cert.pem and key.pem to cert subdirectory")
    void put_must_write_certPem_and_keyPem_to_cert_subdirectory() throws IOException {
        String issuerId = "issuer-1";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        Certificate certificate = context.generator().generate(certSpec);
        datastore.put(certificate);

        Path certDirectory = issuerDirectory.resolve(certificate.getSerial());
        assertTrue(Files.exists(certDirectory));
        Path certFilePath = certDirectory.resolve("cert.pem");
        assertTrue(Files.exists(certFilePath));
        Path keyFilePath = certDirectory.resolve("key.pem");
        assertTrue(Files.exists(keyFilePath));

    }

    @Test
    @DisplayName("listStored() must return list of existing subdirectories containing valid pem files")
    void listStored_must_return_list_of_existing_subdirectories_containing_valid_pem_files() throws IOException {
        String issuerId = "issuer-2";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        Certificate certificate = context.generator().generate(certSpec);
        datastore.put(certificate);

        Path emptyDirectory = issuerDirectory.resolve("0123456789");
        Files.createDirectory(emptyDirectory);

        Path corruptedDirectory = issuerDirectory.resolve("9876543210");
        Files.createDirectory(corruptedDirectory);
        Files.writeString(corruptedDirectory.resolve("cert.pem"), "lorem ipsum");
        Files.writeString(corruptedDirectory.resolve("key.pem"), "lorem ipsum");

        Collection<StoredCert> storedCerts = datastore.listStored();
        assertEquals(1, storedCerts.size());

    }

    @Test
    @DisplayName("getCert() must return DirectoryStoredCert pointing to subdirectory with serial_number")
    void getCert_must_return_DirectoryStoredCert_pointing_to_subdirectory_with_serial_name() throws IOException {

        String issuerId = "issuer-3";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        Certificate certificate = context.generator().generate(certSpec);
        datastore.put(certificate);

        Optional<StoredCert> storedCertOptional = datastore.getCert(certificate.getSerial());
        assertFalse(storedCertOptional.isEmpty());
        StoredCert storedCert = storedCertOptional.get();
        assertTrue(storedCert instanceof DirectoryStoredCert);
        DirectoryStoredCert directoryStoredCert = (DirectoryStoredCert) storedCert;

        Path expectedDirectory = issuerDirectory.resolve(certificate.getSerial());
        assertEquals(expectedDirectory.toAbsolutePath().toString(), directoryStoredCert.getDirectory().getAbsolutePath());

    }


    @Test
    @DisplayName("deleteCert() must delete subdirectory with serial name")
    void deleteCert_must_delete_subdirectory_with_serial_name() throws IOException {

        String issuerId = "issuer-4";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        Certificate certificate = context.generator().generate(certSpec);
        datastore.put(certificate);

        Path expectedDirectory = issuerDirectory.resolve(certificate.getSerial());
        assertTrue(Files.exists(expectedDirectory));
        datastore.deleteCert(certificate.getSerial());
        assertFalse(Files.exists(expectedDirectory));

    }

    @Test
    @DisplayName("putIssuerCertSerial() must write serial to iss_serial file")
    void putIssuerCertSerial_must_write_serial_to_iss_serial_file() throws IOException {

        String issuerId = "issuer-5";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        Path issCertSerialFile = issuerDirectory.resolve(CertConstants.ISSUER_CERT_SERIAL_FILENAME);

        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        assertFalse(Files.exists(issCertSerialFile));
        datastore.putIssuerCertSerial("0123456789");
        assertTrue(Files.exists(issCertSerialFile));
        assertEquals("0123456789", Files.readString(issCertSerialFile, StandardCharsets.UTF_8));

    }


    @Test
    @DisplayName("getIssuerCertSerial() must return contents of iss_serial file")
    void getIssuerCertSerial_must_return_contents_of_iss_serial_file() throws IOException {

        String issuerId = "issuer-6";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        Path issCertSerialFile = issuerDirectory.resolve(CertConstants.ISSUER_CERT_SERIAL_FILENAME);
        Files.writeString(issCertSerialFile,"0123456789");

        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        Optional<String> issCertserialOptional = datastore.getIssuerCertSerial();
        assertFalse(issCertserialOptional.isEmpty());
        String issCertSerial = issCertserialOptional.get();
        assertEquals("0123456789", issCertSerial);

    }



    @Test
    @DisplayName("purge() must delete the issuer directory")
    void purge_must_delete_the_issuer_directory() throws IOException {

        String issuerId = "issuer-7";
        Path issuerDirectory = CERTS_DIRECTORY.resolve(issuerId);
        Files.createDirectories(issuerDirectory);
        Path issCertSerialFile = issuerDirectory.resolve(CertConstants.ISSUER_CERT_SERIAL_FILENAME);
        Files.writeString(issCertSerialFile,"10000100019");

        DirectoryIssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        Certificate certificate = context.generator().generate(certSpec);
        datastore.put(certificate);

        Path emptyDirectory = issuerDirectory.resolve("0000000010");
        Files.createDirectory(emptyDirectory);

        datastore.purge();
        assertFalse(Files.exists(issuerDirectory));

    }

}
