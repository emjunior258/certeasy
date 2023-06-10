package org.certeasy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class SubjectAttributeTypeTest {

    @Test
    @DisplayName("fromOID() must succeed if matching OID")
    public void fromOIDMustSucceedIfMatchingOID(){
        SubjectAttributeType commonName = SubjectAttributeType.fromOID("2.5.4.3");
        assertNotNull(commonName);
        assertEquals(SubjectAttributeType.CommonName, commonName);
        assertEquals("2.5.4.3", commonName.getOid());
        SubjectAttributeType countryName = SubjectAttributeType.fromOID("2.5.4.6");
        assertNotNull(countryName);
        assertEquals(SubjectAttributeType.CountryName, countryName);
        assertEquals("2.5.4.6", countryName.getOid());
    }

    @Test
    @DisplayName("fromOID() must fail if no matching OID")
    public void fromOIDMustFailIfNoMatchingOID(){
        assertThrows(IllegalArgumentException.class, () -> {
            SubjectAttributeType.fromOID("0000000");
        });
    }

    @Test
    @DisplayName("ofKey() must return attribute with matching mnemonic")
    public void ofKeyMustReturnAttributeWithMatchingMnemonic(){
        SubjectAttributeType commonName = SubjectAttributeType.ofKey("CN");
        assertNotNull(commonName);
        assertEquals(SubjectAttributeType.CommonName, commonName);
        SubjectAttributeType countryName = SubjectAttributeType.ofKey("C");
        assertNotNull(countryName);
        assertEquals(SubjectAttributeType.CountryName, countryName);
        SubjectAttributeType state = SubjectAttributeType.ofKey("ST");
        assertNotNull(state);
        assertEquals(SubjectAttributeType.Province, state);
    }

    @Test
    @DisplayName("ofKey() must return attribute with matching description")
    public void ofKeyMustReturnAttributeWithMatchingDescription(){
        SubjectAttributeType commonName = SubjectAttributeType.ofKey("commonName");
        assertNotNull(commonName);
        assertEquals("commonName", commonName.getDescription());
        assertEquals(SubjectAttributeType.CommonName, commonName);
        SubjectAttributeType countryName = SubjectAttributeType.ofKey("countryName");
        assertNotNull(countryName);
        assertEquals("countryName", countryName.getDescription());
        assertEquals(SubjectAttributeType.CountryName, countryName);
        SubjectAttributeType state = SubjectAttributeType.ofKey("stateOrProvinceName");
        assertNotNull(state);
        assertEquals("stateOrProvinceName", state.getDescription());
        assertEquals(SubjectAttributeType.Province, state);
    }

    @Test
    @DisplayName("ofKey() must fail if no matching key")
    public void ofKeyMustFailIfNoMatchingKey(){
        assertThrows(IllegalArgumentException.class, () -> {
            SubjectAttributeType.ofKey("DDD");
        });
    }

    @Test
    @DisplayName("attributeTypes must be ordered as expected")
    public void attributeTypesMustBeOrderedAsExpected(){
        List<SubjectAttributeType> attributes = Arrays.asList(SubjectAttributeType.values());
        attributes.sort(Comparator.naturalOrder());
        Iterator<SubjectAttributeType> iterator = attributes.iterator();
        assertEquals(SubjectAttributeType.CommonName, iterator.next());
        assertEquals(SubjectAttributeType.Title, iterator.next());
        assertEquals(SubjectAttributeType.Initials, iterator.next());
        assertEquals(SubjectAttributeType.GivenName, iterator.next());
        assertEquals(SubjectAttributeType.Surname, iterator.next());
        assertEquals(SubjectAttributeType.TelephoneNumber, iterator.next());
        assertEquals(SubjectAttributeType.UserID, iterator.next());
        assertEquals(SubjectAttributeType.OrganizationName, iterator.next());
        assertEquals(SubjectAttributeType.OrganizationUnit, iterator.next());
        assertEquals(SubjectAttributeType.CountryName, iterator.next());
        assertEquals(SubjectAttributeType.Province, iterator.next());
        assertEquals(SubjectAttributeType.Locality, iterator.next());
        assertEquals(SubjectAttributeType.Street, iterator.next());
        assertEquals(SubjectAttributeType.DomainComponent, iterator.next());
    }

    @Test
    @DisplayName("must match MultiValue attributes")
    public void mustMatchMultivalueAttributes(){
        Set<SubjectAttributeType> attributeTypeSet = Arrays.stream(SubjectAttributeType.values())
                .filter(SubjectAttributeType::isMultiValue)
                .collect(Collectors.toSet());
        assertEquals(4, attributeTypeSet.size());
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.OrganizationUnit));
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.DomainComponent));
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.UserID));
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.GivenName));
    }

}
