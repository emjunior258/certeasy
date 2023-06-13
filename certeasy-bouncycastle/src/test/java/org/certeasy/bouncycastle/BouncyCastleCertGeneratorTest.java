package org.certeasy.bouncycastle;

import org.bouncycastle.operator.OperatorCreationException;
import org.certeasy.*;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BouncyCastleCertGeneratorTest {

    static BouncyCastleCertGenerator generator;
    static CertificateAuthoritySubject subject;
    static CertificateAuthoritySpec spec;
    static CertificateSpec certSpec;
    static Certificate authorityCert;

    @BeforeAll
    static void initAll() {
        generator = new BouncyCastleCertGenerator();
        subject = new CertificateAuthoritySubject("John Tester",
                new GeographicAddress("MZ", "Maputo", "KaMabukwane", "Av. F"));

        DateRange dateRange = new DateRange(LocalDate.now().plusDays(2));
        spec = new CertificateAuthoritySpec(subject,0, KeyStrength.LOW,
                dateRange);

        certSpec = new CertificateSpec(subject, KeyStrength.LOW, dateRange,spec.getBasicConstraints(), spec.getKeyUsages(), null);

//        authorityCert = generator.generate(spec);

    }


    @Test
    @DisplayName("must throw exception with null CertificateAuthoritySpec")
    void mustThrowExceptionWithNullCertificateAuthoritySpec() {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.generate(null);
        });
    }

    @Test
    @DisplayName("must not throw exception with Valid CertificateAuthoritySpec")
    void mustNotThrowExceptionWithValidCertificateAuthoritySpec() {
        assertDoesNotThrow(() -> {
            generator.generate(spec);
        });
    }

    @Test
    @DisplayName("must Throw Exception With Null CertificateSpec And Null Certificate")
    void mustThrowExceptionWithNullCertificateSpecAndNullCertificate() {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.generate(null, null);
        });
    }

    @Test
    @DisplayName("must Throw Exception With Valid CertificateSpec And Null Certificate")
    void mustThrowExceptionWithValidCertificateAuthoritySpecAndNullCertificate() {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.generate(certSpec, null);
        });
    }

    @Test
    @DisplayName("must Throw Exception With Valid CertificateSpec And Authority Certificate")
    void mustNotThrowExceptionWithValidCertificateAuthoritySpecAndValidAuthorityCertificate() {
        authorityCert = generator.generate(spec);
        assertDoesNotThrow(() -> {
            generator.generate(certSpec, authorityCert);
        });
    }
}