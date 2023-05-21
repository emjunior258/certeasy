package org.certeasy.backend.issuer.rest;

import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.HashSet;
import java.util.Set;

public record IssuerCertRef(String issuer, String serial) implements Validable {

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if(issuer == null || issuer.isBlank())
            violations.add(new Violation(path, "issuer", ViolationType.REQUIRED, "issuer must not be be null nor empty"));
        if(serial == null || serial.isBlank())
            violations.add(new Violation(path, "serial", ViolationType.REQUIRED, "serial must not be be null nor empty"));
        return violations;
    }

}
