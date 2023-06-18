package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CertificateSubjectTest {

    @Test
    @DisplayName("constructor must succeed with null alternative names")
    void constructorMustSucceedWithNullAlternativeNames(){
        new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=MZ")
                        .build()
                , null);
    }

    @Test
    @DisplayName("constructor must not succeed with null DistinguishedName")
    void constructorMustNotSucceedWithNullDistinguishedName(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateSubject(
                    null,
                    Set.of(
                            new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "test.example.com"),
                            new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "demo.example.com")
                    ));
        });
    }

    @Test
    @DisplayName("constructor must succeed if distinguished name and alternatives are provided")
    void mustConstructCertificateSubjectSuccessfullyWithDistinguishedNameAndAlternatives(){
        new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=MZ")
                        .build(),
                Set.of(
                        new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "test.example.com"),
                        new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "demo.example.com")
                ));
    }

    @Test
    @DisplayName("constructor must succeed with zero arguments")
    void mustConstructCertificateSubjectWithZeroArguments(){
        new CertificateSubject();
    }

    @Test
    @DisplayName("setDistinguishedName() must change distinguishedName")
    void setDistinguishedNameMustChangeDistinguishedName(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        subject.setDistinguishedName(
                DistinguishedName.builder()
                        .parse("CN=John Doe, C=US")
                        .build());
        DistinguishedName distinguishedName = subject.getDistinguishedName();
        assertEquals("John Doe", distinguishedName.getCommonName());
        Optional<RelativeDistinguishedName> countryName = distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME);
        assertTrue(countryName.isPresent());
        assertEquals("US", countryName.get().value());
    }


    @Test
    @DisplayName("setAlternativeNames() must change alternative names")
    void setAlternativeNamesMustSubjectChangeAlternativeNames(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        subject.setAlternativeNames(Set.of(
                new SubjectAlternativeName(SubjectAlternativeNameType.OTHER_NAME, "Baga Yaga"),
                new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "johnwick.com")
        ));
        Set<String> alternativeNames = subject.getAlternativeNames().stream().map(SubjectAlternativeName::value)
                .collect(Collectors.toSet());
        assertTrue(alternativeNames.contains("Baga Yaga"));
        assertTrue(alternativeNames.contains("johnwick.com"));
    }

    @Test
    @DisplayName("getCommonName() must return DistinguishedName CN attribute")
    void getCommonNameMustReturnDistinguishedNameCN(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        assertEquals("John Wick", subject.getCommonName());
    }

    @Test
    @DisplayName("hasAlternativeNames() must return false if alternative names are not set")
    void hasAlternativeNamesMustReturnFalseIfAlternativeNamesAreNotSet(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        assertFalse(subject.hasAlternativeNames());
    }

    @Test
    @DisplayName("hasAlternativeNames() must return true if alternative names are set")
    void hasAlternativeNamesMustReturnTrueIfAlternativeNamesAreSet(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        subject.setAlternativeNames(Set.of(
                new SubjectAlternativeName(SubjectAlternativeNameType.OTHER_NAME, "Baga Yaga"),
                new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "johnwick.com")
        ));
        assertTrue(subject.hasAlternativeNames());
    }

    @Test
    @DisplayName("getAlternativeNames() must return all alternative names set")
    void getAlternativeNamesMustReturnAllAlternativeNamesSet(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        subject.setAlternativeNames(Set.of(
                new SubjectAlternativeName(SubjectAlternativeNameType.OTHER_NAME, "Baga Yaga"),
                new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "johnwick.com")
        ));

        Set<SubjectAlternativeName> nameSet = subject.getAlternativeNames();
        assertEquals(2, nameSet.size());
    }


}
