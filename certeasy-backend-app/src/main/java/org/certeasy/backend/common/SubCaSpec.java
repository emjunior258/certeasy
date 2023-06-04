package org.certeasy.backend.common;

import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Set;

public class SubCaSpec extends BaseCaCertSpec {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = super.validate(path);
        if(name==null || name.isBlank())
            violations.add(new Violation(path,  "name", ViolationType.REQUIRED, "name is required"));
        if(name != null && name.trim().length()> 128)
            violations.add(new Violation(path,  "name", ViolationType.LENGTH, "name length should not exceed 128 characters"));
        return violations;
    }


}
