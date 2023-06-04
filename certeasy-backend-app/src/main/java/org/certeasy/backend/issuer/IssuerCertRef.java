package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.HashSet;
import java.util.Set;

public record IssuerCertRef(@JsonProperty("issuer_id") String issuerId, String serial) implements Validable {

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if(issuerId == null || issuerId.isBlank())
            violations.add(new Violation(path, "issuer_id", ViolationType.REQUIRED, "issuer_id must not be be null nor empty"));
        if(serial == null || serial.isBlank())
            violations.add(new Violation(path, "serial", ViolationType.REQUIRED, "serial must not be be null nor empty"));
        return violations;
    }

}
