package org.certeasy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RelativeDistinguishedNameTest {

    @Test
    @DisplayName("toString() must return desired output")
    void toStringMustProduceDesiredOutput(){

        String userId = "123456789";
        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.USER_ID,userId);
        Assertions.assertEquals(String.format("%s=%s", SubjectAttributeType.USER_ID.getMnemonic(), userId),
                rdn.toString());

    }

}
