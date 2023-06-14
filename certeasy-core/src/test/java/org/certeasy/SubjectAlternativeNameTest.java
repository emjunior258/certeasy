package org.certeasy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubjectAlternativeNameTest {


    @Test
    @DisplayName("must not allow null type")
    public void must_not_allow_null_type(){
        assertThrows(IllegalArgumentException.class, () -> {
            new SubjectAlternativeName(null, "value");
        });
    }


    @Test
    @DisplayName("must not allow null value")
    public void must_not_allow_null_value(){
        assertThrows(IllegalArgumentException.class, () -> {
            new SubjectAlternativeName(SubjectAlternativeNameType.DNS, null);
        });
    }

    @Test
    @DisplayName("must not allow empty value")
    public void must_not_allow_empty_value(){
        assertThrows(IllegalArgumentException.class, () -> {
            new SubjectAlternativeName(SubjectAlternativeNameType.DNS, "");
        });
    }

}
