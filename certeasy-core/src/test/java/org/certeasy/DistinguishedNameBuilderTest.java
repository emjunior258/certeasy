package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DistinguishedNameBuilderTest {

    @Test
    void musParseDistinguishedNameSuccessfullySet1() {

        DistinguishedName distinguishedName =  DistinguishedName.builder()
                .parse("CN=John Doe, DC=www, DC=example, DC=com, O=Example Organization, C=US")
                .build();

        Set<SubjectAttributeType> attributeTypes = distinguishedName.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.COMMON_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.DOMAIN_COMPONENT));
        assertTrue(attributeTypes.contains(SubjectAttributeType.ORGANIZATION_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.COUNTRY_NAME));

        assertEquals("John Doe", distinguishedName.getCommonName());
        Set<RelativeDistinguishedName> dc = distinguishedName.findAll(SubjectAttributeType.DOMAIN_COMPONENT);
        assertEquals(3, dc.size());

        Optional<RelativeDistinguishedName> org = distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_NAME);
        assertTrue(org.isPresent());
        assertEquals("Example Organization", org.get().value());

        Optional<RelativeDistinguishedName> country = distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME);
        assertTrue(country.isPresent());
        assertEquals("US", country.get().value());

    }

    @Test
    void musParseDistinguishedNameSuccessfullySet2() {

        DistinguishedName distinguishedName =  DistinguishedName.builder()
                .parse("CN=www.example.com, OU=IT Department, O=Dummy Corporation, L=New York, ST=New York, C=US")
                .build();

        Set<SubjectAttributeType> attributeTypes = distinguishedName.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.COMMON_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.ORGANIZATION_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.ORGANIZATION_UNIT));
        assertTrue(attributeTypes.contains(SubjectAttributeType.COUNTRY_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.PROVINCE));
        assertTrue(attributeTypes.contains(SubjectAttributeType.LOCALITY));

        assertEquals("www.example.com", distinguishedName.getCommonName());

        Optional<RelativeDistinguishedName> org = distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_NAME);
        assertTrue(org.isPresent());
        assertEquals("Dummy Corporation", org.get().value());

        Optional<RelativeDistinguishedName> unit = distinguishedName.findFirst(SubjectAttributeType.ORGANIZATION_UNIT);
        assertTrue(unit.isPresent());
        assertEquals("IT Department", unit.get().value());

        Optional<RelativeDistinguishedName> country = distinguishedName.findFirst(SubjectAttributeType.COUNTRY_NAME);
        assertTrue(country.isPresent());
        assertEquals("US", country.get().value());

        Optional<RelativeDistinguishedName> state = distinguishedName.findFirst(SubjectAttributeType.PROVINCE);
        assertTrue(state.isPresent());
        assertEquals("New York", state.get().value());

        Optional<RelativeDistinguishedName> locality = distinguishedName.findFirst(SubjectAttributeType.LOCALITY);
        assertTrue(locality.isPresent());
        assertEquals("New York", locality.get().value());

    }

    @Test
    @DisplayName("parse() must require CN attribute")
    void parseMusRequireCommonName() {
        assertThrows(IllegalArgumentException.class, () -> {
            DistinguishedName.builder()
                    .parse("OU=IT Department, O=Dummy Corporation, L=New York, ST=New York, C=US")
                    .build();
        });
    }


    @Test
    @DisplayName("parse() must reject empty string")
    void parseMusRejectEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            DistinguishedName.builder()
                    .parse("")
                    .build();
        });
    }

    @Test
    @DisplayName("parse() must reject null string")
    void parseMusRejectNullString() {
        assertThrows(IllegalArgumentException.class, () -> {
            DistinguishedName.builder()
                    .parse(null)
                    .build();
        });
    }


    @Test
    @DisplayName("parse() must fail to parse Unknown attribute")
    void mustFailToParseUnknownAttribute() {
        assertThrows(IllegalSubjectAttributeTypeException.class, () -> {
            DistinguishedName.builder()
                    .parse("OUA=IT Department, O=Dummy Corporation")
                    .build();
        });
    }

    @Test
    @DisplayName("append() must cause the RDN to be present in resulting DistinguishedName")
    void appendedRDNMustBePresent() {

        DistinguishedName dn = DistinguishedName.builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.COUNTRY_NAME, "US"))
                .build();

        Set<SubjectAttributeType> attributeTypes = dn.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.COMMON_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.COUNTRY_NAME));

    }

    @Test
    @DisplayName("append() must cause the RDN Set to be present in resulting DistinguishedName")
    void appendedRDNSetMustBePresent() {

        DistinguishedName dn = DistinguishedName.builder()
                .append(Set.of(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME,
                                "John Doe"),
                        new RelativeDistinguishedName(SubjectAttributeType.COUNTRY_NAME,
                                "US")))
                .build();

        Set<SubjectAttributeType> attributeTypes = dn.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.COMMON_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.COUNTRY_NAME));

    }

    @Test
    @DisplayName("append() must cause the Convertible RDNs to be present in resulting DistinguishedName")
    void appendedConvertiblesRDNsMustBePresent() {

        RDNConvertible convertible = Mockito.mock(RDNConvertible.class);
        when(convertible.toRdns()).thenReturn(
                Set.of(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME,
                                "John Doe"),
                        new RelativeDistinguishedName(SubjectAttributeType.COUNTRY_NAME,
                                "US"))
        );

        DistinguishedName dn = DistinguishedName.builder()
                .append(convertible)
                .build();

        Set<SubjectAttributeType> attributeTypes = dn.getAttributeTypes();
        assertTrue(attributeTypes.contains(SubjectAttributeType.COMMON_NAME));
        assertTrue(attributeTypes.contains(SubjectAttributeType.COUNTRY_NAME));

    }



}
