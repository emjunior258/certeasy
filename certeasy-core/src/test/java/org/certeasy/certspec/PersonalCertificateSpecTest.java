package org.certeasy.certspec;

import org.certeasy.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalCertificateSpecTest {

    private PersonalIdentitySubject subject;

    public PersonalCertificateSpecTest(){
        PersonName personName = new PersonName("John", "Doe");
        GeographicAddress address = new GeographicAddress("US", "New York", "Brooklyn", "123 Main St");
        Set<String> emails = new HashSet<>();
        emails.add("john.doe@example.com");
        Set<String> usernames = new HashSet<>();
        usernames.add("johndoe");
        this.subject = new PersonalIdentitySubject(personName, address, "123-456-7890", emails, usernames);
    }


    @Test
    @DisplayName("spec must be constructed and must have all expected attributes")
    public void must_be_constructed_and_must_have_all_expected_attributes() {

        PersonalCertificateSpec spec = new PersonalCertificateSpec(subject, KeyStrength.HIGH, new DateRange(LocalDate.of(2050, Month.JANUARY, 1)));
        assertFalse(spec.getBasicConstraints().ca());
        Set<KeyUsage> keyUsages = spec.getKeyUsages();
        assertFalse(keyUsages.isEmpty());
        assertTrue(keyUsages.contains(KeyUsage.DigitalSignature));
        assertTrue(keyUsages.contains(KeyUsage.NonRepudiation));
        Optional<ExtendedKeyUsages> optionalExtendedKeyUsages = spec.getExtendedKeyUsages();
        assertTrue(optionalExtendedKeyUsages.isPresent());
        ExtendedKeyUsages extendedKeyUsages = optionalExtendedKeyUsages.get();
        assertTrue(extendedKeyUsages.usages().contains(ExtendedKeyUsage.EMAIL_PROTECTION));
        assertTrue(extendedKeyUsages.usages().contains(ExtendedKeyUsage.TLS_WEB_CLIENT_AUTH));
        assertTrue(extendedKeyUsages.usages().contains(ExtendedKeyUsage.SIGN_CODE));

    }

    @Test
    @DisplayName("must fail to construct with null subject")
    public void must_fail_to_construct_with_null_subject(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PersonalCertificateSpec(null, KeyStrength.LOW, new DateRange(LocalDate.of(2050, Month.JANUARY, 1)));
        });
    }

    @Test
    @DisplayName("must fail to construct with null key strength")
    public void must_fail_to_construct_with_null_key_strength(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PersonalCertificateSpec(subject, null, new DateRange(LocalDate.of(2050, Month.JANUARY, 1)));
        });
    }

    @Test
    @DisplayName("must fail to construct with null validity")
    public void must_fail_to_construct_with_null_validity(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PersonalCertificateSpec(subject, KeyStrength.MEDIUM, null);
        });
    }


}
