package org.certeasy.certspec;

import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationBindingTest {

    @Test
    void mustRequireNonNullOrganizationName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrganizationBinding(null, "IT Department",
                    "Software Engineer");
        });
    }

    @Test
    void mustNotRequireTitleAndDepartment() {
        OrganizationBinding binding = new OrganizationBinding("Vodafone", null,
                null);
        assertNotNull(binding);
    }

    @Test
    void mustMapOrganizationNameToOUAttributeType(){
        OrganizationBinding binding = new OrganizationBinding("Vodafone", null,
                null);
        Set<RelativeDistinguishedName> rdns = binding.toRdns();
        assertEquals(1, rdns.size());
        RelativeDistinguishedName o = rdns.iterator().next();
        assertEquals(SubjectAttributeType.ORGANIZATION_NAME, o.type());
        assertEquals("Vodafone", o.value());
    }

    @Test
    void mustMapDepartmentToOUAttributeType(){
        OrganizationBinding binding = new OrganizationBinding("Vodafone", "Financial services",
                null);
        Set<RelativeDistinguishedName> rdns = binding.toRdns();
        assertEquals(2, rdns.size());
        Set<SubjectAttributeType> subjectAttributeTypes = rdns.stream()
                .map(RelativeDistinguishedName::type)
                .collect(Collectors.toSet());
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.ORGANIZATION_NAME));
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.ORGANIZATION_UNIT));
    }

    @Test
    void mustMapTitleAttributeType(){
        OrganizationBinding binding = new OrganizationBinding("Vodafone", "Financial services",
                "Head of Engineering");
        Set<RelativeDistinguishedName> rdns = binding.toRdns();
        assertEquals(3, rdns.size());
        Set<SubjectAttributeType> subjectAttributeTypes = rdns.stream()
                .map(RelativeDistinguishedName::type)
                .collect(Collectors.toSet());
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.ORGANIZATION_NAME));
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.ORGANIZATION_UNIT));
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.TITLE));
    }

}
