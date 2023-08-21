package org.certeasy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
class RelativeDistinguishedNameTest {

    @Test
    @DisplayName("toString() must escape plus sign")
    void toStringMustEscapePlusSign(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario+Junior");
        System.out.println(rdn.toString());
        assertEquals("G=Mario\\+Junior", rdn.toString());

    }

    @Test
    @DisplayName("toString() must escape back slash")
    void toStringMustEscapeBackSlash(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario\\Junior");
        assertEquals("G=Mario\\Junior", rdn.toString());

    }

    @Test
    @DisplayName("toString() must escape pound")
    void toStringMustEscapePound(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario#Junior");
        assertEquals("G=Mario\\#Junior", rdn.toString());

    }

    @Test
    @DisplayName("toString() must escape double quote")
    void toStringMustEscapeDoubleQuote(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario\"Junior");
        assertEquals("G=Mario\\\"Junior", rdn.toString());

    }

    @Test
    @DisplayName("toString() must escape greater than")
    void toStringMustEscapeGreaterThan(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario>Junior");
        assertEquals("G=Mario\\>Junior", rdn.toString());

    }

    @Test
    @DisplayName("toString() must escape less than")
    void toStringMustEscapeLessThan(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario<Junior");
        assertEquals("G=Mario\\<Junior", rdn.toString());

    }

    @Test
    @DisplayName("toString() must escape comma")
    void toStringMustEscapeComma(){

        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME,"Mario,Junior");
        assertEquals("G=Mario\\,Junior", rdn.toString());

    }


}
