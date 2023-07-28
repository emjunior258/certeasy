package org.certeasy.backend.persistence;


import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import static org.junit.jupiter.api.Assertions.*;

import org.certeasy.CertEasyContext;
import org.certeasy.Certificate;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.persistence.directory.DirectoryIssuerDatastore;
import org.certeasy.backend.persistence.directory.DirectoryIssuerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

@QuarkusTest
@TestProfile(DirectoryPersistenceProfile.class)
public class DirectoryIssuerRegistryTest extends DirectoryBaseTest {

    @Inject
    CertEasyContext context;


    private IssuerRegistry createRegistry(){
        return new DirectoryIssuerRegistry(DATA_DIRECTORY.toString(),
                context);
    }

    @Test
    @DisplayName("fail to create instance with invalid arguments")
    void fail_to_create_instance_with_invalid_arguments(){
        assertThrows(IllegalArgumentException.class, () -> new DirectoryIssuerRegistry(null, context));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryIssuerRegistry("lorem", context));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryIssuerRegistry("pom.xml", context));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryIssuerRegistry("", context));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryIssuerRegistry("target", null));
    }

    @Test
    @DisplayName("create instance with valid arguments")
    void create_instance_with_valid_arguments(){
        DirectoryIssuerRegistry registry = new DirectoryIssuerRegistry("target", context);
        assertNotNull(registry);
    }

    @Test
    @DisplayName("list() must return collection of existing well-formed directories")
    void list_must_return_collection_of_existing_well_formed_directories() throws IOException {
        Path issuer1Directory = DATA_DIRECTORY.resolve("issuer-1");
        Files.createDirectory(issuer1Directory);
        Path issuer2Directory = DATA_DIRECTORY.resolve("issuer-2");
        Files.createDirectories(issuer2Directory);
        Path issuer3Directory = DATA_DIRECTORY.resolve("issuer-3");
        Files.createDirectory(issuer3Directory);

        Certificate certificate = context.generator().generate(certSpec);
        DirectoryIssuerDatastore issuerDatastore = new DirectoryIssuerDatastore(issuer1Directory.toFile(), context);
        issuerDatastore.putIssuerCertSerial(certificate.getSerial());
        issuerDatastore.put(certificate);

        IssuerRegistry registry = createRegistry();
        Collection<CertIssuer> certIssuers = registry.list();
        assertEquals(1, certIssuers.size());

    }

    @Test
    @DisplayName("add() must throw when arguments not valid")
    void add_must_throw_when_arguments_not_valid(){
        Certificate certificate = context.generator().generate(certSpec);
        IssuerRegistry registry = createRegistry();

        assertThrows(IllegalArgumentException.class, () -> registry.add(null), "certificate MUST not be null");
//        assertThrows(IllegalArgumentException.class, () -> registry.add("example", certificate),"dir MUST not be null and MUST point to an existing directory");
    }

    @Test
    @DisplayName("add() must store issuer and certificate in directory")
    void add_must_store_issuer_and_certificate_in_directory(){

        Certificate certificate = context.generator().generate(certSpec);
        Path issuerDirectory = DATA_DIRECTORY.resolve(certificate.getDistinguishedName().digest());
        assertFalse(Files.exists(issuerDirectory));
        IssuerRegistry registry = createRegistry();
        registry.add(certificate);
        assertTrue(Files.exists(issuerDirectory));
        Path certDirectory = issuerDirectory.resolve(certificate.getSerial());
        assertTrue(Files.exists(certDirectory));
        Path keyPem = certDirectory.resolve("cert.pem");
        assertTrue(Files.exists(keyPem));
        Path certPem = certDirectory.resolve("key.pem");
        assertTrue(Files.exists(certPem));

    }

    @Test
    @DisplayName("exists() must throw when issuer id empty or null")
    void exists_must_throw_when_issuer_id_empty_or_null() throws IOException {
        IssuerRegistry registry = createRegistry();
        assertThrows(IllegalArgumentException.class, () -> registry.exists(""), "name MUST not be null");
        assertThrows(IllegalArgumentException.class, () -> registry.exists(null), "name MUST not be null");

    }

    @Test
    @DisplayName("exists() must check if subdirectory exists and is well-formed")
    void exists_must_must_check_if_subdirectory_exists_and_is_well_formed() throws IOException {

        Path issuerXDirectory = DATA_DIRECTORY.resolve("issuer-x");
        Files.createDirectory(issuerXDirectory);
        Path issuerYDirectory = DATA_DIRECTORY.resolve("issuer-y");
        Files.createDirectories(issuerYDirectory);

        Certificate certificate = context.generator().generate(certSpec);
        DirectoryIssuerDatastore issuerDatastore = new DirectoryIssuerDatastore(issuerXDirectory.toFile(), context);
        issuerDatastore.putIssuerCertSerial(certificate.getSerial());
        issuerDatastore.put(certificate);

        IssuerRegistry registry = createRegistry();
        assertTrue(registry.exists("issuer-x"));
        assertFalse(registry.exists("issuer-y"));
        assertFalse(registry.exists("issuer-z"));

    }

    @Test
    @DisplayName("getById() must throw when issuer id is null or empty")
    void getById_must_throw_when_issuer_id_null_or_empty() throws IOException {
        IssuerRegistry registry = createRegistry();
        assertThrows(IllegalArgumentException.class, () -> registry.getById(""), "name MUST not be null");
        assertThrows(IllegalArgumentException.class, () -> registry.getById(null), "name MUST not be null");

    }


    @Test
    @DisplayName("getById() must check if subdirectory exists and is well-formed")
    void getById_must_check_if_subdirectory_exists_and_is_well_formed() throws IOException {

        Path firstIssuerDirectory = DATA_DIRECTORY.resolve("first");
        Files.createDirectory(firstIssuerDirectory);
        Path secondIssuerDirectory = DATA_DIRECTORY.resolve("second");
        Files.createDirectories(secondIssuerDirectory);

        Certificate certificate = context.generator().generate(certSpec);
        DirectoryIssuerDatastore issuerDatastore = new DirectoryIssuerDatastore(firstIssuerDirectory.toFile(), context);
        issuerDatastore.putIssuerCertSerial(certificate.getSerial());
        issuerDatastore.put(certificate);

        IssuerRegistry registry = createRegistry();
        assertTrue(registry.getById("first").isPresent());
        assertFalse(registry.getById("second").isPresent());
        assertFalse(registry.getById("third").isPresent());

    }

    @Test
    @DisplayName("delete() must throw when issuer id null")
    void delete_must_throw_when_issuer_id_null() {
        IssuerRegistry registry = createRegistry();
        assertThrows(IllegalArgumentException.class, () -> registry.delete(null), "issuerId MUST not be null");
    }

    @Test
    @DisplayName("delete() must remove issuer directory")
    void delete_must_remove_the_issuer_directory() throws IOException {

        Path issuerDirectory = DATA_DIRECTORY.resolve("example");
        Files.createDirectory(issuerDirectory);

        Certificate certificate = context.generator().generate(certSpec);
        DirectoryIssuerDatastore issuerDatastore = new DirectoryIssuerDatastore(issuerDirectory.toFile(), context);
        issuerDatastore.putIssuerCertSerial(certificate.getSerial());
        issuerDatastore.put(certificate);

        IssuerRegistry registry = createRegistry();
        Optional<CertIssuer> optionalCertIssuer = registry.getById("example");
        assertFalse(optionalCertIssuer.isEmpty());
        CertIssuer issuer = optionalCertIssuer.get();
        registry.delete(issuer);

        assertFalse(Files.exists(issuerDirectory));

    }


}
