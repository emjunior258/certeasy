package org.certeasy.backend.common.cert;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.persistence.MemoryPersistenceProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(MemoryPersistenceProfile.class)
class CertValidityTest {

    @Test
    void must_not_add_violations_for_valid_dates() {
        CertValidity certValidity = new CertValidity("2023-01-01", "2099-12-31");
        assertTrue(certValidity.validate(ValidationPath.of("body")).isEmpty());
    }

    @Test
    void must_add_violations_for_null_or_empty_dates() {
        CertValidity certValidity = new CertValidity(null, null);
        assertEquals(2, certValidity.validate(ValidationPath.of("body")).size());

        CertValidity certValidityEmpty = new CertValidity("", " ");
        assertEquals(2, certValidityEmpty.validate(ValidationPath.of("body")).size());

        CertValidity certValidityNonIso = new CertValidity("2023-", "2023- ");
        assertEquals(2, certValidityNonIso.validate(ValidationPath.of("body")).size());

        CertValidity certValidityNonIso2 = new CertValidity("2023-01-01", "2023-00-00");
        assertEquals(1, certValidityNonIso2.validate(ValidationPath.of("body")).size());
    }

    @Test
    void must_add_violations_for_non_iso_dates() {

        CertValidity certValidityNonIso = new CertValidity("2023-", "2023- ");
        assertEquals(2, certValidityNonIso.validate(ValidationPath.of("body")).size());

        CertValidity certValidityNonIso2 = new CertValidity("2023-01-01", "2023-00-00");
        assertEquals(1, certValidityNonIso2.validate(ValidationPath.of("body")).size());
    }

    @Test
    void must_add_violations_for_precedence() {

        CertValidity certValidity = new CertValidity("2023-01-01", "2022-01-01 ");
        assertEquals(1, certValidity.validate(ValidationPath.of("body")).size());
    }

}