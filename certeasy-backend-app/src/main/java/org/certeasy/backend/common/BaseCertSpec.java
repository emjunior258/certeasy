package org.certeasy.backend.common;

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

public abstract class BaseCertSpec implements Validable {
    
    private CertValidity validity;
    @JsonProperty("key_strength")
    private String keyStrength;
    @JsonProperty("address")
    private GeographicAddressInfo geographicAddressInfo;

    public CertValidity getValidity() {
        return validity;
    }

    public void setValidity(CertValidity validity) {
        this.validity = validity;
    }

    public String getKeyStrength() {
        return keyStrength;
    }

    public void setKeyStrength(String keyStrength) {
        this.keyStrength = keyStrength;
    }

    public GeographicAddressInfo getGeographicAddressInfo() {
        return geographicAddressInfo;
    }

    public void setGeographicAddressInfo(GeographicAddressInfo geographicAddressInfo) {
        this.geographicAddressInfo = geographicAddressInfo;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
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
        if(geographicAddressInfo==null)
            violations.add(new Violation(path,"address", ViolationType.REQUIRED, "address MUST not be null"));
        if(geographicAddressInfo!=null)
            violations.addAll(geographicAddressInfo.validate(path.append("address")));

        return violations;
    }
}
