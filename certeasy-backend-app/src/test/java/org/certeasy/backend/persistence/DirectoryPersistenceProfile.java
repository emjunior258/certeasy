package org.certeasy.backend.persistence;

import io.quarkus.test.junit.QuarkusTestProfile;
import org.certeasy.backend.CertConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;

public class DirectoryPersistenceProfile implements QuarkusTestProfile {

    static {
        Path path = DirectoryBaseTest.DATA_DIRECTORY; //Path.of("target","test");
        if (Files.notExists(path)) {
            try {
                System.out.println("Creating directory {"+ path.toAbsolutePath() + "}");
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<String, String> getConfigOverrides(){
        return Map.ofEntries(Map.entry(CertConstants.DATA_DIRECTORY_CONFIG, DirectoryBaseTest.DATA_DIRECTORY.toString()));
    }

}
