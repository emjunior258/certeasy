package org.certeasy.certspec;

import org.certeasy.DistinguishedName;
import org.certeasy.GeographicAddress;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CertificateAuthoritySubjectTest {

    @Test
    public void constructorMustNotAcceptNullName(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CertificateAuthoritySubject(null,
                    new GeographicAddress("MZ", "Maputo", "Kampfumo", "Av. 24 de Julho"));
        });
    }


    @Test
    public void constructorMustNotAcceptNullAddress(){
        assertThrows(IllegalArgumentException.class, () -> {
            String name = "example.com";
            new CertificateAuthoritySubject(name,
                    null);
        });
    }


    @Test
    public void mustSetDistinguishedNameWithSubjectAttributes(){
        String name = "lorem-ipsum";
        GeographicAddress address = new GeographicAddress("MZ", "Maputo", "Kampfumo", "Av. 24 de Julho");
        CertificateAuthoritySubject subject = new CertificateAuthoritySubject(name,
                address);
        DistinguishedName dn = subject.getDistinguishedName();
        Optional<RelativeDistinguishedName> cn = dn.findFirst(SubjectAttributeType.CommonName);
        assertTrue(cn.isPresent());

        Optional<RelativeDistinguishedName> countryName = dn.findFirst(SubjectAttributeType.CountryName);
        assertTrue(countryName.isPresent());
        assertEquals(address.countryIsoCode(), countryName.get().value());

        Optional<RelativeDistinguishedName> province = dn.findFirst(SubjectAttributeType.Province);
        assertTrue(province.isPresent());
        assertEquals(address.state(), province.get().value());

        Optional<RelativeDistinguishedName> locality = dn.findFirst(SubjectAttributeType.Locality);
        assertTrue(locality.isPresent());
        assertEquals(address.locality(), locality.get().value());

        Optional<RelativeDistinguishedName> street = dn.findFirst(SubjectAttributeType.Street);
        assertTrue(street.isPresent());
        assertEquals(address.streetAddress(), street.get().value());

        Set<SubjectAttributeType> nameSet = dn.getAttributeTypes();
        assertEquals(5, nameSet.size());

    }

}
