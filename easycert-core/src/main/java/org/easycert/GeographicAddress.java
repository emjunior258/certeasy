package org.easycert;

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
        return Set.of(
                new RelativeDistinguishedName(SubjectAttributeType.CountryName, countryIsoCode),
                new RelativeDistinguishedName(SubjectAttributeType.Province, state),
                new RelativeDistinguishedName(SubjectAttributeType.Locality, locality),
                new RelativeDistinguishedName(SubjectAttributeType.Street, streetAddress)
        );
    }


}
