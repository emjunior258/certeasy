package org.certeasy.backend.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.KeyStrength;
import org.certeasy.backend.common.cert.GeographicAddressInfo;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class GeographicCertSpec extends BaseCertSpec {

    @JsonProperty("address")
    private GeographicAddressInfo geographicAddressInfo;

    public GeographicAddressInfo getGeographicAddressInfo() {
        return geographicAddressInfo;
    }

    public void setGeographicAddressInfo(GeographicAddressInfo geographicAddressInfo) {
        this.geographicAddressInfo = geographicAddressInfo;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>(super.validate(path));
        if (geographicAddressInfo == null)
            violations.add(new Violation(path, "address", ViolationType.REQUIRED, "address MUST not be null"));
        if (geographicAddressInfo != null)
            violations.addAll(geographicAddressInfo.validate(path.append("address")));
        return violations;
    }

}
