package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DistinguishedNameTest {

    @Test
    @DisplayName("toString() must return desired output: set1")
    void toStringMustReturnDesiredOutputSet1(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.INITIALS, "JD"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "vm", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "co", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "mz", 2))
                .build();

        assertEquals("CN=John Doe, initials=JD, DC=vm, DC=co, DC=mz", dn.toString());

    }

    @Test
    @DisplayName("toString() must return desired output: set2")
    void toStringMustReturnDesiredOutputSet2(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.INITIALS, "JD"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME, "John"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.SURNAME, "Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.COUNTRY_NAME, "MZ"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.PROVINCE, "Maputo"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.LOCALITY, "Kampfumo"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.STREET, "Av 24 de Julho. Nr300"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.USER_ID, "johndoe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.TELEPHONE_NUMBER, "849901010"))
                .build();
        assertEquals("CN=John Doe, initials=JD, G=John, SN=Doe, telephoneNumber=849901010, UID=johndoe, C=MZ, ST=Maputo, L=Kampfumo, STREET=Av 24 de Julho. Nr300",
                dn.toString());
    }

    @Test
    @DisplayName("toString() must return desired output: set3")
    void toStringMustReturnDesiredOutputSet3(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "John Wick"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.TITLE, "Mr"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_NAME, "High Table"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_UNIT, "Continental Hotel"))
                .build();
        assertEquals("CN=John Wick, T=Mr, O=High Table, OU=Continental Hotel", dn.toString());
    }


    @Test
    @DisplayName("findAll() must return all RDNs of specified type in expected order")
    void findAllMustFindAllRDNsOfSpecifiedTypeInExpectedOrder(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Example"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "www", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "example", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "com", 2))
                .build();

        Set<RelativeDistinguishedName> domainComponents = dn.findAll(SubjectAttributeType.DOMAIN_COMPONENT);
        assertEquals(3, domainComponents.size());

        Iterator<RelativeDistinguishedName> iterator = domainComponents.iterator();
        RelativeDistinguishedName rdn = iterator.next();
        assertEquals(SubjectAttributeType.DOMAIN_COMPONENT, rdn.type());
        assertEquals("www", rdn.value());

        rdn = iterator.next();
        assertEquals(SubjectAttributeType.DOMAIN_COMPONENT, rdn.type());
        assertEquals("example", rdn.value());

        rdn = iterator.next();
        assertEquals(SubjectAttributeType.DOMAIN_COMPONENT, rdn.type());
        assertEquals("com", rdn.value());

    }

    @Test
    @DisplayName("findAll() must return empty set of RDNs of specified type")
    void findAllMustReturnEmptySetOfSpecifiedType(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Tim Cook"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME, "Tim"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.SURNAME, "Cook"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.TITLE, "Mr"))
                .build();
        assertTrue(dn.findAll(SubjectAttributeType.DOMAIN_COMPONENT).isEmpty());

    }


    @Test
    @DisplayName("findFirst() must return first matching RDN")
    void findFirstMustReturnFirstMatchingRDN(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Example"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "www", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "example", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DOMAIN_COMPONENT, "com", 2))
                .build();

        Optional<RelativeDistinguishedName> matched = dn.findFirst(SubjectAttributeType.DOMAIN_COMPONENT);
        assertTrue(matched.isPresent());
        assertEquals("www", matched.get().value());

    }

    @Test
    @DisplayName("findFirst() must return empty optional")
    void findFirstMustReturnEmptyOptional(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Example"))
                .build();

        Optional<RelativeDistinguishedName> matched = dn.findFirst(SubjectAttributeType.DOMAIN_COMPONENT);
        assertTrue(matched.isEmpty());

    }

    @Test
    @DisplayName("hasAttribute() must return true if attribute is present")
    void hasAttributeMustReturnTrueIfAttributeIsPresent(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Example"))
                .build();
        assertTrue(dn.hasAttribute(SubjectAttributeType.COMMON_NAME));
        assertTrue(dn.hasAttribute(SubjectAttributeType.COMMON_NAME, "Example"));
    }

    @Test
    @DisplayName("hasAttribute() must return false if attribute is not present")
    void hasAttributeMustReturnFalseIfAttributeIsNotPresent(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Example"))
                .build();
        assertFalse(dn.hasAttribute(SubjectAttributeType.DOMAIN_COMPONENT));
        assertFalse(dn.hasAttribute(SubjectAttributeType.COMMON_NAME, "Example_"));
    }


    @Test
    @DisplayName("digest() must return SHA-1 hash")
    void digestMustReturnSHA1Hash(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, "Tim Cook"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.GIVEN_NAME, "Tim"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.SURNAME, "Cook"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.TITLE, "Mr"))
                .build();
        assertEquals("b251a05ff27591302ff3d24df5dfdcc2b71e0e31", dn.digest());
    }


}
