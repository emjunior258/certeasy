package org.certeasy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CertificateSpecTest {

    private CertificateSubject subject;
    private DateRange validityPeriod;

    public CertificateSpecTest(){
        this.subject = new CertificateSubject(DistinguishedName.builder()
                .parse("CN=example.com, C=US")
                .build());
        this.validityPeriod = new DateRange(
                LocalDate.of(2023, Month.JANUARY, 1),
                LocalDate.of(2023, Month.DECEMBER, 31)
        );
    }

    @Test
    @DisplayName("constructor must require non null subject argument")
    public void mustRequireNonNullSubject(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSpec(null, KeyStrength.LOW, validityPeriod,
                    new BasicConstraints(false),
                    Set.of(KeyUsage.KeyAgreement), new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION))
            );
        });
    }

    @Test
    @DisplayName("constructor must require non null keyStrength argument")
    public void mustRequireNonNullKeyStrength(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSpec(subject, null, validityPeriod,
                    new BasicConstraints(false),
                    Set.of(KeyUsage.KeyAgreement), new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION))
            );
        });
    }

    @Test
    @DisplayName("constructor must require non null validityPeriod argument")
    public void mustRequireNonNullValidityPeriod(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSpec(subject, KeyStrength.LOW, null,
                    new BasicConstraints(false),
                    Set.of(KeyUsage.KeyAgreement), new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION))
            );
        });
    }

    @Test
    @DisplayName("constructor must require non null basicConstraints argument")
    public void mustRequireNonNullBasicConstraints(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSpec(subject, KeyStrength.LOW, validityPeriod,
                    null, Set.of(KeyUsage.DecipherOnly),
                    new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION))
            );
        });
    }

    @Test
    @DisplayName("constructor must require non null keyUsages argument")
    public void mustRequireNonNullKeyUsages(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSpec(subject, KeyStrength.LOW, validityPeriod,
                    new BasicConstraints(false),
                    null, new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION))
            );
        });
    }

    @Test
    @DisplayName("constructor must require non empty keyUsages argument")
    public void mustRequireNonEmptyKeyUsagesSet(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSpec(subject, KeyStrength.LOW, validityPeriod,
                    new BasicConstraints(false), new HashSet<>(),
                    new ExtendedKeyUsages(Set.of(
                            ExtendedKeyUsage.EMAIL_PROTECTION))
            );
        });
    }

    @Test
    @DisplayName("constructor must allow null extendedKeyUsages argument")
    public void mustAllowNullExtendedKeyUsages(){
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.LOW, validityPeriod,
                new BasicConstraints(false), Set.of(KeyUsage.CertificateSign),
                null);
        assertFalse(spec.getExtendedKeyUsages().isPresent());
    }

    @Test
    @DisplayName("getters must return values supplied to constructor")
    public void gettersMustReturnConstructorArguments(){
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.LOW, validityPeriod,
                new BasicConstraints(false), Set.of(KeyUsage.CertificateSign, KeyUsage.DigitalSignature),
                new ExtendedKeyUsages(Set.of(
                        ExtendedKeyUsage.EMAIL_PROTECTION))
        );
        assertEquals(subject, spec.getSubject());
        assertEquals(KeyStrength.LOW, spec.getKeyStrength());
        assertEquals(validityPeriod, spec.getValidityPeriod());
        BasicConstraints basicConstraints = spec.getBasicConstraints();
        assertFalse(basicConstraints.ca());
        Set<KeyUsage> keyUsageSet = spec.getKeyUsages();
        assertEquals(2, keyUsageSet.size());
        Optional<ExtendedKeyUsages> extendedKeyUsages = spec.getExtendedKeyUsages();
        assertTrue(extendedKeyUsages.isPresent());
        Set<ExtendedKeyUsage> extendedKeyUsageSet = extendedKeyUsages.get().usages();
        assertEquals(1, extendedKeyUsageSet.size());
    }

}
