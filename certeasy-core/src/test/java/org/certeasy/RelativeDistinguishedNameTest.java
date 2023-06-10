package org.certeasy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RelativeDistinguishedNameTest {

    @Test
    @DisplayName("toString() must return desired output")
    public void toStringMustProduceDesiredOutput(){

        String userId = "123456789";
        RelativeDistinguishedName rdn = new RelativeDistinguishedName(SubjectAttributeType.UserID,userId);
        Assertions.assertEquals(String.format("%s=%s", SubjectAttributeType.UserID.getMnemonic(), userId),
                rdn.toString());

    }

}
