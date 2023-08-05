package org.certeasy.backend.persistence;

import org.certeasy.DateRange;
import org.certeasy.GeographicAddress;
import org.certeasy.KeyStrength;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class DirectoryBaseTest {

    static final Path DATA_DIRECTORY = Path.of("target","test");

    CertificateAuthoritySpec certSpec;

    public DirectoryBaseTest(){
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
        try(Stream<Path> fileStream= Files.list(DATA_DIRECTORY)) {
            fileStream.forEach(DirectoryBaseTest::deleteRecursive);
        }catch (IOException ex){
            fail("Failed to cleanup test directory", ex);
        }
    }
    private static void deleteRecursive(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (Stream<Path> stream = Files.list(path)) {
                    stream.forEach(DirectoryBaseTest::deleteRecursive);
                }
            }
            Files.deleteIfExists(path);
            System.out.println("Deleted: "+path.toAbsolutePath().toString());
        }catch (IOException ex){
            fail("error deleting: "+path.toAbsolutePath().toString(), ex);
        }
    }

}
