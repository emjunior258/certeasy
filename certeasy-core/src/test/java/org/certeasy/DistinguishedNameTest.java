package org.certeasy;

import org.certeasy.DistinguishedName;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DistinguishedNameTest {

    @Test
    @DisplayName("toString() must return desired output: set1")
    public void toStringMustReturnDesiredOutputSet1(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Initials, "JD"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "vm", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "co", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "mz", 2))
                .build();

        assertEquals("CN=John Doe, initials=JD, DC=vm, DC=co, DC=mz", dn.toString());

    }

    @Test
    @DisplayName("toString() must return desired output: set2")
    public void toStringMustReturnDesiredOutputSet2(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Initials, "JD"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.GivenName, "John"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Surname, "Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.CountryName, "MZ"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Province, "Maputo"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Locality, "Kampfumo"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Street, "Av 24 de Julho. Nr300"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.UserID, "johndoe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.TelephoneNumber, "849901010"))
                .build();
        assertEquals("CN=John Doe, initials=JD, G=John, SN=Doe, telephoneNumber=849901010, UID=johndoe, C=MZ, ST=Maputo, L=Kampfumo, STREET=Av 24 de Julho. Nr300",
                dn.toString());
    }

    @Test
    @DisplayName("toString() must return desired output: set3")
    public void toStringMustReturnDesiredOutputSet3(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "John Wick"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Title, "Mr"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.OrganizationName, "High Table"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.OrganizationUnit, "Continental Hotel"))
                .build();
        assertEquals("CN=John Wick, T=Mr, O=High Table, OU=Continental Hotel", dn.toString());
    }


    @Test
    @DisplayName("findAll() must return all RDNs of specified type in expected order")
    public void findAllMustFindAllRDNsOfSpecifiedTypeInExpectedOrder(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "Example"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "www", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "example", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "com", 2))
                .build();

        Set<RelativeDistinguishedName> domainComponents = dn.findAll(SubjectAttributeType.DomainComponent);
        assertEquals(3, domainComponents.size());

        Iterator<RelativeDistinguishedName> iterator = domainComponents.iterator();
        RelativeDistinguishedName rdn = iterator.next();
        assertEquals(SubjectAttributeType.DomainComponent, rdn.type());
        assertEquals("www", rdn.value());

        rdn = iterator.next();
        assertEquals(SubjectAttributeType.DomainComponent, rdn.type());
        assertEquals("example", rdn.value());

        rdn = iterator.next();
        assertEquals(SubjectAttributeType.DomainComponent, rdn.type());
        assertEquals("com", rdn.value());

    }

    @Test
    @DisplayName("findAll() must return empty set of RDNs of specified type")
    public void findAllMustReturnEmptySetOfSpecifiedType(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "Tim Cook"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.GivenName, "Tim"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Surname, "Cook"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Title, "Mr"))
                .build();
        assertTrue(dn.findAll(SubjectAttributeType.DomainComponent).isEmpty());

    }


    @Test
    @DisplayName("findFirst() must return first matching RDN")
    public void findFirstMustReturnFirstMatchingRDN(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "Example"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "www", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "example", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "com", 2))
                .build();

        Optional<RelativeDistinguishedName> matched = dn.findFirst(SubjectAttributeType.DomainComponent);
        assertTrue(matched.isPresent());
        assertEquals("www", matched.get().value());

    }

    @Test
    @DisplayName("findFirst() must return empty optional")
    public void findFirstMustReturnEmptyOptional(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "Example"))
                .build();

        Optional<RelativeDistinguishedName> matched = dn.findFirst(SubjectAttributeType.DomainComponent);
        assertTrue(matched.isEmpty());

    }

    @Test
    @DisplayName("hasAttribute() must return true if attribute is present")
    public void hasAttributeMustReturnTrueIfAttributeIsPresent(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "Example"))
                .build();
        assertTrue(dn.hasAttribute(SubjectAttributeType.CommonName));
        assertTrue(dn.hasAttribute(SubjectAttributeType.CommonName, "Example"));
    }

    @Test
    @DisplayName("hasAttribute() must return false if attribute is not present")
    public void hasAttributeMustReturnFalseIfAttributeIsNotPresent(){
        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "Example"))
                .build();
        assertFalse(dn.hasAttribute(SubjectAttributeType.DomainComponent));
        assertFalse(dn.hasAttribute(SubjectAttributeType.CommonName, "Example_"));
    }


}
