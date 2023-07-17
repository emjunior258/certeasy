package org.certeasy.backend.persistence;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.Set;

public class MemoryPersistenceProfile  implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "memory-persistence";
    }

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return Collections.singleton(MapIssuerRegistry.class);
    }

}
