package org.certeasy.backend.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.HashSet;
import java.util.Set;

public record OrganizationInfo (

    @JsonProperty("organization_name")
    String organizationName,

    @JsonProperty("organization_unit")
    String organizationUnit) implements Validable {

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if(organizationName == null || organizationName.isBlank())
            violations.add(new Violation(path, "organization_name", ViolationType.REQUIRED,
                    "country cannot be blank"));
        if(organizationUnit != null && organizationUnit.isBlank())
            violations.add(new Violation(path, "organization_unit", ViolationType.FORMAT,
                    "organization_unit cannot be blank"));
        return violations;
    }


}
