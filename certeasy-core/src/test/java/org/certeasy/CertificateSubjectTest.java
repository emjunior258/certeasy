package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateSubjectTest {

    @Test
    @DisplayName("constructor must succeed with null alternative names")
    public void constructorMustSucceedWithNullAlternativeNames(){
        new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=MZ")
                        .build()
                , null);
    }

    @Test
    @DisplayName("constructor must not succeed with null DistinguishedName")
    public void constructorMustNotSucceedWithNullDistinguishedName(){
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
    public void mustConstructCertificateSubjectSuccessfullyWithDistinguishedNameAndAlternatives(){
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
    public void mustConstructCertificateSubjectWithZeroArguments(){
        new CertificateSubject();
    }

    @Test
    @DisplayName("setDistinguishedName() must change distinguishedName")
    public void setDistinguishedNameMustChangeDistinguishedName(){
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
        Optional<RelativeDistinguishedName> countryName = distinguishedName.findFirst(SubjectAttributeType.CountryName);
        assertTrue(countryName.isPresent());
        assertEquals("US", countryName.get().value());
    }


    @Test
    @DisplayName("setAlternativeNames() must change alternative names")
    public void setAlternativeNamesMustSubjectChangeAlternativeNames(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        subject.setAlternativeNames(Set.of(
                new SubjectAlternativeName(SubjectAlternativeNameType.OTHER_NAME, "Baga Yaga"),
                new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "johnwick.com")
        ));
    }

    @Test
    @DisplayName("getCommonName() must return DistinguishedName CN attribute")
    public void getCommonNameMustReturnDistinguishedNameCN(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        assertEquals("John Wick", subject.getCommonName());
    }

    @Test
    @DisplayName("hasAlternativeNames() must return false if alternative names are not set")
    public void hasAlternativeNamesMustReturnFalseIfAlternativeNamesAreNotSet(){
        CertificateSubject subject =  new CertificateSubject(
                DistinguishedName.builder()
                        .parse("CN=John Wick, C=UK")
                        .build());
        assertFalse(subject.hasAlternativeNames());
    }

    @Test
    @DisplayName("hasAlternativeNames() must return true if alternative names are set")
    public void hasAlternativeNamesMustReturnTrueIfAlternativeNamesAreSet(){
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
    public void getAlternativeNamesMustReturnAllAlternativeNamesSet(){
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
