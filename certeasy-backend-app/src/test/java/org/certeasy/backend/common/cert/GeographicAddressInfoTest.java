package org.certeasy.backend.common.cert;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.persistence.MemoryPersistenceProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(MemoryPersistenceProfile.class)
class GeographicAddressInfoTest {

    @Test
    void must_not_add_violations_for_valid_geographic_address() {
        GeographicAddressInfo geographicAddressInfo = new GeographicAddressInfo("US", "California", "Mountain View", "1600 Amphitheatre Parkway");
        assertTrue(geographicAddressInfo.validate(ValidationPath.of("body")).isEmpty());
    }

    @Test
    void must_add_violations_for_null_or_empty_country() {
        GeographicAddressInfo geographicAddressInfo = new GeographicAddressInfo(null, "California", "Mountain View", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo.validate(ValidationPath.of("body")).size());

        GeographicAddressInfo geographicAddressInfo2 = new GeographicAddressInfo(" ", "California", "Mountain View", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo2.validate(ValidationPath.of("body")).size());
    }
    @Test
    void must_add_violations_for_invalid_country() {
        GeographicAddressInfo geographicAddressInfo = new GeographicAddressInfo("USA", "California", "Mountain View", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo.validate(ValidationPath.of("body")).size());

        GeographicAddressInfo geographicAddressInfo2 = new GeographicAddressInfo("us ", "California", "Mountain View", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo2.validate(ValidationPath.of("body")).size());
    }

    @Test
    void must_add_violations_for_null_or_empty_state() {
        GeographicAddressInfo geographicAddressInfo = new GeographicAddressInfo("US", null, "Mountain View", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo.validate(ValidationPath.of("body")).size());

        GeographicAddressInfo geographicAddressInfo2 = new GeographicAddressInfo("US ", " ", "Mountain View", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo2.validate(ValidationPath.of("body")).size());
    }

    @Test
    void must_add_violations_for_null_or_empty_locality() {
        GeographicAddressInfo geographicAddressInfo = new GeographicAddressInfo("US", "California", null, "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo.validate(ValidationPath.of("body")).size());

        GeographicAddressInfo geographicAddressInfo2 = new GeographicAddressInfo("US ", "California", "  ", "1600 Amphitheatre Parkway");
        assertEquals(1, geographicAddressInfo2.validate(ValidationPath.of("body")).size());
    }

    @Test
    void must_add_violations_for_null_or_empty_street_address() {
        GeographicAddressInfo geographicAddressInfo = new GeographicAddressInfo("US", "California", "Mountain View", null);
        assertEquals(1, geographicAddressInfo.validate(ValidationPath.of("body")).size());

        GeographicAddressInfo geographicAddressInfo2 = new GeographicAddressInfo("US ", "California ", "Mountain View", "  ");
        assertEquals(1, geographicAddressInfo2.validate(ValidationPath.of("body")).size());
    }
}