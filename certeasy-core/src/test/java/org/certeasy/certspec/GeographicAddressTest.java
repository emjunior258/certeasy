package org.certeasy.certspec;

import org.certeasy.GeographicAddress;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GeographicAddressTest {

    @Test
    void toRdnsMustReturnCountryAttribute() {
        GeographicAddress address = new GeographicAddress("US", null, null, null);
        Set<RelativeDistinguishedName> nameSet = address.toRdns();
        assertEquals(1, nameSet.size());
        Iterator<RelativeDistinguishedName> iterator = nameSet.iterator();
        RelativeDistinguishedName country = iterator.next();
        assertEquals("US", country.value());
    }

    @Test
    void toRdnsMustReturnCountryProvinceAttributes() {
        GeographicAddress address = new GeographicAddress("MZ", "Maputo", null, null);
        Set<RelativeDistinguishedName> nameSet = address.toRdns();
        assertEquals(2, nameSet.size());
        Map<SubjectAttributeType, String> map = nameSet.stream().collect(Collectors.toMap(RelativeDistinguishedName::type,
                RelativeDistinguishedName::value));
        assertTrue(map.containsKey(SubjectAttributeType.PROVINCE));
        String province = map.get(SubjectAttributeType.PROVINCE);
        assertEquals("Maputo", province);
        assertTrue(map.containsKey(SubjectAttributeType.COUNTRY_NAME));
        String countryName = map.get(SubjectAttributeType.COUNTRY_NAME);
        assertEquals("MZ", countryName);
    }

    @Test
    void toRdnsMustReturnCountryProvinceLocalityStreetAttributes() {
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
