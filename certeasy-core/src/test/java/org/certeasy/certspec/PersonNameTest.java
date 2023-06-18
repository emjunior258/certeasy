package org.certeasy.certspec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PersonNameTest {

    private PersonName personName;

    PersonNameTest(){
        this.personName = new PersonName("John", "Doe");
    }

    @Test
    @DisplayName("fullName() must match expected value")
    void fullName_must_match_expectedValue() {
        Assertions.assertEquals("John Doe", personName.fullName());
    }

    @Test
    @DisplayName("Should get initials for person name")
    void initials_must_match_expected_value() {
        PersonName personName = new PersonName("John", "Tester");
        Assertions.assertEquals("JT", personName.initials());
    }

    @Test
    @DisplayName("givenName() must match expected value")
    void givenName_must_match_expected_value() {
        Assertions.assertEquals("John", personName.givenName());
    }

    @Test
    @DisplayName("surname() must match expected value")
    void surname_must_match_expected_value() {
        Assertions.assertEquals("Doe", personName.surname());
    }
}