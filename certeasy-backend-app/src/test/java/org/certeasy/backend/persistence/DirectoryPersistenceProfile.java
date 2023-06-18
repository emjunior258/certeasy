package org.certeasy.backend.persistence;

import io.quarkus.test.junit.QuarkusTestProfile;
import org.certeasy.backend.CertConstants;

import java.util.Map;

public class DirectoryPersistenceProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides(){
        return Map.ofEntries(Map.entry(CertConstants.DATA_DIRECTORY_CONFIG, "target/test"));
    }

}
