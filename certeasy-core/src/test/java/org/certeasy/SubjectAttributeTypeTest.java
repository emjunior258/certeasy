package org.certeasy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

class SubjectAttributeTypeTest {

    @Test
    @DisplayName("fromOID() must succeed if matching OID")
    void fromOIDMustSucceedIfMatchingOID(){
        SubjectAttributeType commonName = SubjectAttributeType.fromOID("2.5.4.3");
        assertNotNull(commonName);
        assertEquals(SubjectAttributeType.COMMON_NAME, commonName);
        assertEquals("2.5.4.3", commonName.getOid());
        SubjectAttributeType countryName = SubjectAttributeType.fromOID("2.5.4.6");
        assertNotNull(countryName);
        assertEquals(SubjectAttributeType.COUNTRY_NAME, countryName);
        assertEquals("2.5.4.6", countryName.getOid());
    }

    @Test
    @DisplayName("fromOID() must fail if no matching OID")
    void fromOIDMustFailIfNoMatchingOID(){
        assertThrows(IllegalArgumentException.class, () -> {
            SubjectAttributeType.fromOID("0000000");
        });
    }

    @Test
    @DisplayName("ofKey() must return attribute with matching mnemonic")
    void ofKeyMustReturnAttributeWithMatchingMnemonic(){
        SubjectAttributeType commonName = SubjectAttributeType.ofKey("CN");
        assertNotNull(commonName);
        assertEquals(SubjectAttributeType.COMMON_NAME, commonName);
        SubjectAttributeType countryName = SubjectAttributeType.ofKey("C");
        assertNotNull(countryName);
        assertEquals(SubjectAttributeType.COUNTRY_NAME, countryName);
        SubjectAttributeType state = SubjectAttributeType.ofKey("ST");
        assertNotNull(state);
        assertEquals(SubjectAttributeType.PROVINCE, state);
    }

    @Test
    @DisplayName("ofKey() must return attribute with matching description")
    void ofKeyMustReturnAttributeWithMatchingDescription(){
        SubjectAttributeType commonName = SubjectAttributeType.ofKey("commonName");
        assertNotNull(commonName);
        assertEquals("commonName", commonName.getDescription());
        assertEquals(SubjectAttributeType.COMMON_NAME, commonName);
        SubjectAttributeType countryName = SubjectAttributeType.ofKey("countryName");
        assertNotNull(countryName);
        assertEquals("countryName", countryName.getDescription());
        assertEquals(SubjectAttributeType.COUNTRY_NAME, countryName);
        SubjectAttributeType state = SubjectAttributeType.ofKey("stateOrProvinceName");
        assertNotNull(state);
        assertEquals("stateOrProvinceName", state.getDescription());
        assertEquals(SubjectAttributeType.PROVINCE, state);
    }

    @Test
    @DisplayName("ofKey() must fail if no matching key")
    void ofKeyMustFailIfNoMatchingKey(){
        assertThrows(IllegalArgumentException.class, () -> {
            SubjectAttributeType.ofKey("DDD");
        });
    }

    @Test
    @DisplayName("attributeTypes must be ordered as expected")
    void attributeTypesMustBeOrderedAsExpected(){
        List<SubjectAttributeType> attributes = Arrays.asList(SubjectAttributeType.values());
        attributes.sort(Comparator.naturalOrder());
        Iterator<SubjectAttributeType> iterator = attributes.iterator();
        assertEquals(SubjectAttributeType.COMMON_NAME, iterator.next());
        assertEquals(SubjectAttributeType.TITLE, iterator.next());
        assertEquals(SubjectAttributeType.INITIALS, iterator.next());
        assertEquals(SubjectAttributeType.GIVEN_NAME, iterator.next());
        assertEquals(SubjectAttributeType.SURNAME, iterator.next());
        assertEquals(SubjectAttributeType.TELEPHONE_NUMBER, iterator.next());
        assertEquals(SubjectAttributeType.USER_ID, iterator.next());
        assertEquals(SubjectAttributeType.ORGANIZATION_NAME, iterator.next());
        assertEquals(SubjectAttributeType.ORGANIZATION_UNIT, iterator.next());
        assertEquals(SubjectAttributeType.COUNTRY_NAME, iterator.next());
        assertEquals(SubjectAttributeType.PROVINCE, iterator.next());
        assertEquals(SubjectAttributeType.LOCALITY, iterator.next());
        assertEquals(SubjectAttributeType.STREET, iterator.next());
        assertEquals(SubjectAttributeType.DOMAIN_COMPONENT, iterator.next());
    }

    @Test
    @DisplayName("must match MultiValue attributes")
    void mustMatchMultivalueAttributes(){
        Set<SubjectAttributeType> attributeTypeSet = Arrays.stream(SubjectAttributeType.values())
                .filter(SubjectAttributeType::isMultiValue)
                .collect(Collectors.toSet());
        assertEquals(4, attributeTypeSet.size());
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.ORGANIZATION_UNIT));
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.DOMAIN_COMPONENT));
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.USER_ID));
        assertTrue(attributeTypeSet.contains(SubjectAttributeType.GIVEN_NAME));
    }

}
