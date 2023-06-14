package org.certeasy.certspec;

import org.certeasy.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

class CertificateSpecTest {

    CertificateSpecTest(){

    }


    @Test
    @DisplayName("constructor must allow null ExtendedKeyUsages")
    void constructorMustAllowNullExtendedKeyUsages(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=MZ")
                        .build()
                , null);
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.HIGH, new DateRange(LocalDate.of(2023, Month.AUGUST,1)),new BasicConstraints(10), Set.of(KeyUsage.ENCIPHER_ONLY), null);
        assertNotNull(spec);
    }


    @Test
    @DisplayName("must return provided subject")
    void mustReturnProvidedSubject(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=Armando Guebuza, C=MZ")
                        .build()
                , null);
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.HIGH, new DateRange(LocalDate.of(2023, Month.AUGUST,1)),new BasicConstraints(10), Set.of(KeyUsage.ENCIPHER_ONLY), null);
        assertNotNull(spec);
        assertEquals("CN=Armando Guebuza, C=MZ", subject.getDistinguishedName().toString());
    }

    @Test
    @DisplayName("must return provided KeyStrength")
    void mustReturnProvidedKeyStrength(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=Andrew Tate, C=RO")
                        .build()
                , null);
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.LOW, new DateRange(LocalDate.of(2023, Month.AUGUST,1)),new BasicConstraints(10), Set.of(KeyUsage.ENCIPHER_ONLY), null);
        assertNotNull(spec);
        assertEquals(KeyStrength.LOW, spec.getKeyStrength());
    }

    @Test
    @DisplayName("must return provided Validity period")
    void mustReturnProvidedValidityPeriod(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=Jordan B. Peterson, C=CA")
                        .build()
                , null);
        DateRange dateRange = new DateRange(LocalDate.of(2023, Month.JANUARY,1), LocalDate.of(2023, Month.DECEMBER,31));
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.HIGH, dateRange,new BasicConstraints(10), Set.of(KeyUsage.ENCIPHER_ONLY), null);
        assertNotNull(spec);
        assertEquals(dateRange, spec.getValidityPeriod());
    }

    @Test
    @DisplayName("must return provided BasicConstraints")
    void mustReturnProvidedBasicConstraints(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=Jordan B. Peterson, ST=Ontario, C=CA")
                        .build()
                , null);
        DateRange dateRange = new DateRange(LocalDate.of(2023, Month.JANUARY,1), LocalDate.of(2023, Month.DECEMBER,31));
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.HIGH, dateRange,new BasicConstraints(10), Set.of(KeyUsage.ENCIPHER_ONLY), null);
        assertNotNull(spec);
        assertEquals(new BasicConstraints(10), spec.getBasicConstraints());
    }

    @Test
    @DisplayName("must return provided KeyUsage")
    void mustReturnProvidedKeyUsage(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=Emanuel Macron, C=FR")
                        .build()
                , null);
        DateRange dateRange = new DateRange(LocalDate.of(2023, Month.JANUARY,1), LocalDate.of(2023, Month.DECEMBER,31));
        CertificateSpec spec = new CertificateSpec(subject, KeyStrength.HIGH, dateRange,new BasicConstraints(10), Set.of(KeyUsage.CERTIFICATE_SIGN), null);
        assertNotNull(spec);
        Set<KeyUsage> keyUsages = spec.getKeyUsages();
        assertTrue(keyUsages.contains(KeyUsage.CERTIFICATE_SIGN));
    }

    @Test
    @DisplayName("must return provided ExtendedKeyUsages")
    void mustReturnProvidedExtendedKeyUsages(){
        CertificateSubject subject = new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=Cristiano Ronaldo")
                        .build()
                , null);
        DateRange dateRange = new DateRange(LocalDate.of(2063, Month.DECEMBER,31));
        CertificateSpec spec1 = new CertificateSpec(subject, KeyStrength.MEDIUM, dateRange,new BasicConstraints(5), Set.of(KeyUsage.CERTIFICATE_SIGN), null);
        assertFalse(spec1.getExtendedKeyUsages().isPresent());
        CertificateSpec spec2 = new CertificateSpec(subject, KeyStrength.MEDIUM, dateRange,new BasicConstraints(5), Set.of(KeyUsage.CERTIFICATE_SIGN), new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.EMAIL_PROTECTION)));
        Optional<ExtendedKeyUsages> extendedKeyUsagesOptional = spec2.getExtendedKeyUsages();
        assertTrue(extendedKeyUsagesOptional.isPresent());
        Set<ExtendedKeyUsage> extendedKeyUsageSet = extendedKeyUsagesOptional.get().usages();
        assertTrue(extendedKeyUsageSet.contains(ExtendedKeyUsage.EMAIL_PROTECTION));
    }



}
