package org.certeasy.certspec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PersonNameTest {

    @Test
    void fullname() {
    }

    @Test
    @DisplayName("Should get initials for person name")
    void initials() {
        PersonName personName = new PersonName("John", "Tester");
        Assertions.assertEquals("JT", personName.initials());
    }

    @Test
    void givenName() {
    }

    @Test
    void surname() {
    }
}