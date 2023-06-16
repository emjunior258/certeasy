package org.certeasy;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an address to a Geographic location
 * @param countryIsoCode the ISO code of the country
 * @param state the name of the state/province
 * @param locality the name of the locality in the state/provide
 * @param streetAddress the exact street address in the locality
 */
public record GeographicAddress(String countryIsoCode,
                                String state,
                                String locality,
                                String streetAddress) implements RDNConvertible {

    @Override
    public Set<RelativeDistinguishedName> toRdns() {
        Set<RelativeDistinguishedName> nameSet = new HashSet<>();
        if(countryIsoCode != null)
            nameSet.add(new RelativeDistinguishedName(SubjectAttributeType.COUNTRY_NAME, countryIsoCode));
        if(state != null)
            nameSet.add(new RelativeDistinguishedName(SubjectAttributeType.PROVINCE, state));
        if(locality != null)
            nameSet.add(new RelativeDistinguishedName(SubjectAttributeType.LOCALITY, locality));
        if(streetAddress != null)
            nameSet.add(new RelativeDistinguishedName(SubjectAttributeType.STREET, streetAddress));
        return nameSet;
    }


}
