package org.certeasy.backend.common.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.GeographicAddress;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.HashSet;
import java.util.Set;

public record GeographicAddressInfo(
        @JsonProperty("country") String country,
        @JsonProperty("state") String state,
        @JsonProperty("locality") String locality,
        @JsonProperty("street_address") String streetAddress
) implements Validable {

    private static final String STATE_REGEX_PATTERN = "[A-Z]{2}";

    public GeographicAddressInfo {
    }


    public GeographicAddress toGeographicAddress() {
        return new GeographicAddress(country, state, locality, streetAddress);
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if(country == null || country.isBlank())
            violations.add(new Violation(path, "country", ViolationType.REQUIRED,
                    "country cannot be blank"));
        else if(!country.trim().matches(STATE_REGEX_PATTERN))
            violations.add(new Violation(path, "country", ViolationType.PATTERN,
                    "country does not match Regex pattern /[A-Z]{2}/"));
        if(state == null || state.isBlank())
            violations.add(new Violation(path, "state", ViolationType.REQUIRED,
                    "state cannot be blank"));
        if(locality == null || locality.isBlank())
            violations.add(new Violation(path, "locality", ViolationType.REQUIRED,
                    "locality cannot be blank"));
        if(streetAddress == null || streetAddress.isBlank())
            violations.add(new Violation(path, "street_address", ViolationType.REQUIRED,
                    "street address cannot be blank"));
        return violations;
    }
}
