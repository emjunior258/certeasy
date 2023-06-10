package org.certeasy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class SubjectAttributeTypeTest {

    @Test
    @DisplayName("fromOID() must succeed if matching OID")
    public void fromOIDMustSucceedIfMatchingOID(){
        SubjectAttributeType commonName = SubjectAttributeType.fromOID("2.5.4.3");
        assertNotNull(commonName);
        assertEquals(SubjectAttributeType.CommonName, commonName);
        SubjectAttributeType countryName = SubjectAttributeType.fromOID("2.5.4.6");
        assertNotNull(countryName);
        assertEquals(SubjectAttributeType.CountryName, countryName);
    }

    @Test
    @DisplayName("fromOID() must fail if no matching OID")
    public void fromOIDMustFailIfNoMatchingOID(){
        assertThrows(IllegalArgumentException.class, () -> {
            SubjectAttributeType.fromOID("0000000");
        });
    }

    @Test
    @DisplayName("ofKey() must return attribute with matching key")
    public void ofKeyMustReturnAttributeWithMatchingKey(){
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
    @DisplayName("ofKey() must fail if no matching key")
    public void ofKeyMustFailIfNoMatchingKey(){
        assertThrows(IllegalArgumentException.class, () -> {
            SubjectAttributeType.ofKey("DDD");
        });
    }

}
