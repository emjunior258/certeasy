package org.certeasy.certspec;

import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationBindingTest {

    @Test
    public void mustRequireNonNullOrganizationName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrganizationBinding(null, "IT Department",
                    "Software Engineer");
        });
    }

    @Test
    public void mustNotRequireTitleAndDepartment() {
        new OrganizationBinding("Vodafone", null,
                null);
    }

    @Test
    public void mustMapOrganizationNameToOUAttributeType(){
        OrganizationBinding binding = new OrganizationBinding("Vodafone", null,
                null);
        Set<RelativeDistinguishedName> rdns = binding.toRdns();
        assertEquals(1, rdns.size());
        RelativeDistinguishedName o = rdns.iterator().next();
        assertEquals(SubjectAttributeType.OrganizationName, o.type());
        assertEquals("Vodafone", o.value());
    }

    @Test
    public void mustMapDepartmentToOUAttributeType(){
        OrganizationBinding binding = new OrganizationBinding("Vodafone", "Financial services",
                null);
        Set<RelativeDistinguishedName> rdns = binding.toRdns();
        assertEquals(2, rdns.size());
        Set<SubjectAttributeType> subjectAttributeTypes = rdns.stream()
                .map(RelativeDistinguishedName::type)
                .collect(Collectors.toSet());
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.OrganizationName));
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.OrganizationUnit));
    }

    @Test
    public void mustMapTitleAttributeType(){
        OrganizationBinding binding = new OrganizationBinding("Vodafone", "Financial services",
                "Head of Engineering");
        Set<RelativeDistinguishedName> rdns = binding.toRdns();
        assertEquals(3, rdns.size());
        Set<SubjectAttributeType> subjectAttributeTypes = rdns.stream()
                .map(RelativeDistinguishedName::type)
                .collect(Collectors.toSet());
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.OrganizationName));
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.OrganizationUnit));
        assertTrue(subjectAttributeTypes.contains(SubjectAttributeType.Title));
    }

}
