package org.certeasy.certspec;


import org.certeasy.GeographicAddress;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TLSServerSubjectTest {


    @Test
    public void must_fail_to_create_subject_with_null_name(){
        assertThrows(IllegalArgumentException.class, () -> {
            new TLSServerSubject(null, Set.of("example.org"), new GeographicAddress("MZ", "Lorem", "Ipsum", "Dolor"),
                    "Example Inc");
        });
    }


    @Test
    public void must_fail_to_create_subject_with_null_address(){
        assertThrows(IllegalArgumentException.class, () -> {
            new TLSServerSubject("example", Set.of("example.org"), null,
                    "Example Inc");
        });
    }

    @Test
    public void must_successfully_create_subject_without_organization_name(){
        new TLSServerSubject("example", Set.of("example.org"), new GeographicAddress("MZ", "Lorem", "Ipsum", "Dolor"),
                null);
    }

    @Test
    public void must_fail_to_create_subject_with_empty_domains_set(){
        assertThrows(IllegalArgumentException.class, () -> {
            new TLSServerSubject("example", Collections.emptySet(), new GeographicAddress("MZ", "Lorem", "Ipsum", "Dolor"),
                    "Dummy Inc");
        });
    }

}
