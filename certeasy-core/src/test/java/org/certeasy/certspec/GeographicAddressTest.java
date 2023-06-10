package org.certeasy.certspec;

import org.certeasy.GeographicAddress;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GeographicAddressTest {

    @Test
    public void toRdnsMustReturnCountryAttribute() {
        GeographicAddress address = new GeographicAddress("US", null, null, null);
        Set<RelativeDistinguishedName> nameSet = address.toRdns();
        assertEquals(1, nameSet.size());
        Iterator<RelativeDistinguishedName> iterator = nameSet.iterator();
        RelativeDistinguishedName country = iterator.next();
        assertEquals("US", country.value());
    }

    @Test
    public void toRdnsMustReturnCountryProvinceAttributes() {
        GeographicAddress address = new GeographicAddress("MZ", "Maputo", null, null);
        Set<RelativeDistinguishedName> nameSet = address.toRdns();
        assertEquals(2, nameSet.size());
        Iterator<RelativeDistinguishedName> iterator = nameSet.iterator();
        RelativeDistinguishedName rdn = iterator.next();
        if(rdn.type().equals(SubjectAttributeType.Province)){
            assertEquals("Maputo", rdn.value());
        }
        if(rdn.type().equals(SubjectAttributeType.CountryName)){
            assertEquals("MZ", rdn.value());
        }
    }

    @Test
    public void toRdnsMustReturnCountryProvinceLocalityStreetAttributes() {
        GeographicAddress address = new GeographicAddress("MZ", "Maputo", "Kampfumo", "Av. 25 de Setembro");
        Set<RelativeDistinguishedName> nameSet = address.toRdns().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(LinkedHashSet::new));
        assertEquals(4, nameSet.size());
        Iterator<RelativeDistinguishedName> iterator = nameSet.iterator();
        RelativeDistinguishedName country = iterator.next();
        assertEquals("MZ", country.value());
        RelativeDistinguishedName province = iterator.next();
        assertEquals("Maputo", province.value());
        RelativeDistinguishedName locality = iterator.next();
        assertEquals("Kampfumo", locality.value());
        RelativeDistinguishedName street = iterator.next();
        assertEquals("Av. 25 de Setembro", street.value());
    }

}
