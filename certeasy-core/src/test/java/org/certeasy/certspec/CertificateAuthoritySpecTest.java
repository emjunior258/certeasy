package org.certeasy.certspec;

import org.certeasy.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CertificateAuthoritySpecTest {

    private CertificateAuthoritySubject subject;
    private DateRange validityPeriod;

    CertificateAuthoritySpecTest(){
        this.validityPeriod = new DateRange(LocalDate.of(2023, Month.JANUARY, 1),
                LocalDate.of(3023, Month.DECEMBER, 31));
        this.subject = new CertificateAuthoritySubject("lorem-ipsum",
                new GeographicAddress("MZ", "Maputo", "KaMabukwane",
                        "Av. Fernao Magalhaes")
        );
    }

    @Test
    void mustOnlyHaveCARelatedKeyUsages(){
        CertificateAuthoritySpec spec = new CertificateAuthoritySpec(subject, 0, KeyStrength.LOW,
                validityPeriod);
        Set<KeyUsage> keyUsages = spec.getKeyUsages();
        assertEquals(2, keyUsages.size());
        assertTrue(keyUsages.contains(KeyUsage.CERTIFICATE_SIGN));
        assertTrue(keyUsages.contains(KeyUsage.SIGN_CRL));
    }

    @Test
    void mustHaveCaBasicConstraints(){
        CertificateAuthoritySpec spec = new CertificateAuthoritySpec(subject, 0, KeyStrength.LOW,
                validityPeriod);
        BasicConstraints basicConstraints = spec.getBasicConstraints();
        assertNotNull(basicConstraints);
        assertTrue(basicConstraints.ca());
        assertEquals(0, basicConstraints.pathLength());
    }

    @Test
    void specMustNotHaveAnyExtendedKeyUsages(){
        CertificateAuthoritySpec spec = new CertificateAuthoritySpec(subject, 0, KeyStrength.LOW,
                validityPeriod);
        Optional<ExtendedKeyUsages> extendedKeyUsages =  spec.getExtendedKeyUsages();
        assertTrue(extendedKeyUsages.isEmpty());
    }

}
