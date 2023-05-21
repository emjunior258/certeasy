package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.KeyStrength;
import org.certeasy.backend.common.cert.CertValidity;
import org.certeasy.backend.common.cert.GeographicAddressInfo;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public record IssuerCertInfo (
        String name, CertValidity validity,
        @JsonProperty("key_strength")
        String keyStrength,
        @JsonProperty("address")
        GeographicAddressInfo geographicAddressInfo,
        @JsonProperty("path_length")
        int pathLength ) implements Validable {

        @Override
        public Set<Violation> validate(ValidationPath path) {
                Set<Violation> violations = new HashSet<>();
                if(name==null || name.isBlank())
                        violations.add(new Violation(path,  "name", ViolationType.REQUIRED, "name is required"));
                if(name != null && name.trim().length()> 128)
                        violations.add(new Violation(path,  "name", ViolationType.LENGTH, "name length should not exceed 128 characters"));
                if(validity==null)
                        violations.add(new Violation(path, "validity", ViolationType.REQUIRED, "validity is required"));
                if(validity!=null)
                        violations.addAll(validity.validate(path.append("validity")));
                if(keyStrength==null)
                        violations.add(new Violation(path, "key_strength", ViolationType.REQUIRED, "key_strength is required"));
                if(keyStrength!=null) {
                    try {
                        KeyStrength.valueOf(keyStrength);
                    } catch (IllegalArgumentException e) {
                        violations.add(new Violation(path, "key_strength", ViolationType.ENUM, "key_strength should be one of " + Arrays.toString(KeyStrength.values())));
                    }
                }
                if(geographicAddressInfo==null)
                        violations.add(new Violation(path, "address", ViolationType.REQUIRED, "location is required"));
                if(pathLength<-1)
                        violations.add(new Violation(path, "path_length", ViolationType.RANGE, "path_length should be >= -1"));
                if(geographicAddressInfo==null)
                        violations.add(new Violation(path,"address", ViolationType.REQUIRED, "address MUST not be null"));
                if(geographicAddressInfo!=null)
                        violations.addAll(geographicAddressInfo.validate(path.append("address")));

                return violations;


        }
}
