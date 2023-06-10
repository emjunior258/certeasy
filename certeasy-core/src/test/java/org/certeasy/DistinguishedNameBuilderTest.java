package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DistinguishedNameBuilderTest {

    @Test
    public void musParseDistinguishedNameSuccessfullySet1() {

        DistinguishedName distinguishedName =  DistinguishedName.builder()
                .parse("CN=John Doe, DC=www, DC=example, DC=com, O=Example Organization, C=US")
                .build();

        Set<SubjectAttributeType> attributeTypes = distinguishedName.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.CommonName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.DomainComponent));
        assertTrue(attributeTypes.contains(SubjectAttributeType.OrganizationName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.CountryName));

        assertEquals("John Doe", distinguishedName.getCommonName());
        Set<RelativeDistinguishedName> dc = distinguishedName.findAll(SubjectAttributeType.DomainComponent);
        assertEquals(3, dc.size());

        Optional<RelativeDistinguishedName> org = distinguishedName.findFirst(SubjectAttributeType.OrganizationName);
        assertTrue(org.isPresent());
        assertEquals("Example Organization", org.get().value());

        Optional<RelativeDistinguishedName> country = distinguishedName.findFirst(SubjectAttributeType.CountryName);
        assertTrue(country.isPresent());
        assertEquals("US", country.get().value());

    }

    @Test
    public void musParseDistinguishedNameSuccessfullySet2() {

        DistinguishedName distinguishedName =  DistinguishedName.builder()
                .parse("CN=www.example.com, OU=IT Department, O=Dummy Corporation, L=New York, ST=New York, C=US")
                .build();

        Set<SubjectAttributeType> attributeTypes = distinguishedName.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.CommonName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.OrganizationName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.OrganizationUnit));
        assertTrue(attributeTypes.contains(SubjectAttributeType.CountryName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.Province));
        assertTrue(attributeTypes.contains(SubjectAttributeType.Locality));

        assertEquals("www.example.com", distinguishedName.getCommonName());

        Optional<RelativeDistinguishedName> org = distinguishedName.findFirst(SubjectAttributeType.OrganizationName);
        assertTrue(org.isPresent());
        assertEquals("Dummy Corporation", org.get().value());

        Optional<RelativeDistinguishedName> unit = distinguishedName.findFirst(SubjectAttributeType.OrganizationUnit);
        assertTrue(unit.isPresent());
        assertEquals("IT Department", unit.get().value());

        Optional<RelativeDistinguishedName> country = distinguishedName.findFirst(SubjectAttributeType.CountryName);
        assertTrue(country.isPresent());
        assertEquals("US", country.get().value());

        Optional<RelativeDistinguishedName> state = distinguishedName.findFirst(SubjectAttributeType.Province);
        assertTrue(state.isPresent());
        assertEquals("New York", state.get().value());

        Optional<RelativeDistinguishedName> locality = distinguishedName.findFirst(SubjectAttributeType.Locality);
        assertTrue(locality.isPresent());
        assertEquals("New York", locality.get().value());

    }

    @Test
    @DisplayName("parse() must require CN attribute")
    public void parseMusRequireCommonName() {
        assertThrows(IllegalArgumentException.class, () -> {
            DistinguishedName.builder()
                    .parse("OU=IT Department, O=Dummy Corporation, L=New York, ST=New York, C=US")
                    .build();
        });
    }


    @Test
    @DisplayName("parse() must reject empty string")
    public void parseMusRejectEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            DistinguishedName.builder()
                    .parse("")
                    .build();
        });
    }

    @Test
    @DisplayName("parse() must reject null string")
    public void parseMusRejectNullString() {
        assertThrows(IllegalArgumentException.class, () -> {
            DistinguishedName.builder()
                    .parse(null)
                    .build();
        });
    }


    @Test
    @DisplayName("parse() must fail to parse Unknown attribute")
    public void mustFailToParseUnknownAttribute() {
        assertThrows(IllegalSubjectAttributeTypeException.class, () -> {
            DistinguishedName.builder()
                    .parse("OUA=IT Department, O=Dummy Corporation")
                    .build();
        });
    }

    @Test
    @DisplayName("append() must cause the RDN to be present in resulting DistinguishedName")
    public void appendedRDNMustBePresent() {

        DistinguishedName dn = DistinguishedName.builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.CountryName, "US"))
                .build();

        Set<SubjectAttributeType> attributeTypes = dn.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.CommonName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.CountryName));

    }

    @Test
    @DisplayName("append() must cause the RDN Set to be present in resulting DistinguishedName")
    public void appendedRDNSetMustBePresent() {

        DistinguishedName dn = DistinguishedName.builder()
                .append(Set.of(new RelativeDistinguishedName(SubjectAttributeType.CommonName,
                                "John Doe"),
                        new RelativeDistinguishedName(SubjectAttributeType.CountryName,
                                "US")))
                .build();

        Set<SubjectAttributeType> attributeTypes = dn.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.CommonName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.CountryName));

    }

    @Test
    @DisplayName("append() must cause the Convertible RDNs to be present in resulting DistinguishedName")
    public void appendedConvertiblesRDNsMustBePresent() {

        RDNConvertible convertible = Mockito.mock(RDNConvertible.class);
        when(convertible.toRdns()).thenReturn(
                Set.of(new RelativeDistinguishedName(SubjectAttributeType.CommonName,
                                "John Doe"),
                        new RelativeDistinguishedName(SubjectAttributeType.CountryName,
                                "US"))
        );

        DistinguishedName dn = DistinguishedName.builder()
                .append(convertible)
                .build();

        Set<SubjectAttributeType> attributeTypes = dn.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.CommonName));
        assertTrue(attributeTypes.contains(SubjectAttributeType.CountryName));

    }



}
