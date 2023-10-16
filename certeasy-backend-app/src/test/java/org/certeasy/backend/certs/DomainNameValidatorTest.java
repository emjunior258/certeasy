package org.certeasy.backend.certs;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DomainNameValidatorTest {

    @Test
    void validator_must_return_false_when_domain_null_or_empty() {
        assertFalse(DomainNameValidator.isValidDomain(null));
        assertFalse(DomainNameValidator.isValidDomain(""));
    }

    @Test
    void validator_must_return_false_when_domain_segment_length_invalid() {
        assertFalse(DomainNameValidator.isValidDomain("."));
        assertFalse(DomainNameValidator.isValidDomain("domain.a"));
        StringBuilder longDomain = new StringBuilder("long.domain.co.mz");
        for (int i = 0; i < 125; i++) {
            longDomain.insert(0, "sub.");

        }
        assertFalse(DomainNameValidator.isValidDomain(String.valueOf(longDomain)));
    }

    @Test
    void validator_must_return_false_when_domain_segment_empty(){
        assertFalse(DomainNameValidator.isValidDomain(".com"));
        assertFalse(DomainNameValidator.isValidDomain(" .com"));
        assertFalse(DomainNameValidator.isValidDomain("domain."));
        assertFalse(DomainNameValidator.isValidDomain("domain. "));
    }

    @Test
    void validator_must_return_false_when_domain_segment_contains_special_or_uppercase_chars(){
        assertFalse(DomainNameValidator.isValidDomain("Domain.com"));
        assertFalse(DomainNameValidator.isValidDomain("Doma!n.com"));
    }

    @Test
    void validator_must_return_false_when_domain_segment_starts_or_ends_with_dash(){
        assertFalse(DomainNameValidator.isValidDomain("-domain.com"));
        assertFalse(DomainNameValidator.isValidDomain("domain.co.mz-"));
    }

    @Test
    void validator_must_return_false_when_domain_segment_starts_with_digit(){
        assertFalse(DomainNameValidator.isValidDomain("domain.2go.com"));
    }

    @Test
    void validator_must_return_true_when_domain_is_valid() {
        String[] domains = {
                "domain.co",
                "domain.co.mz",
                "mz",
                "localhost",
                "sub.domain.com",
                "dom.mail2.com",
                "test-domain.com"
        };

        for (String domain:
             domains) {

            assertTrue(DomainNameValidator.isValidDomain(domain));
        }
    }
}