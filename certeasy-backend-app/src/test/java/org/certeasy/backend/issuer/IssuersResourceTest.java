package org.certeasy.backend.issuer;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.certeasy.backend.persistence.MemoryPersistenceProfile;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(MemoryPersistenceProfile.class)
public class IssuersResourceTest {


    @Test
    public void dummy(){

    }

}
