package org.certeasy.backend.certs;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.SubjectAlternativeName;
import org.certeasy.SubjectAlternativeNameType;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@RegisterForReflection
public record AlternativeNameInfo(String type, String value) implements Validable {

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if(value == null || value.isBlank())
            violations.add(new Violation(path, "value",
                    ViolationType.REQUIRED,
                    "alternative name MUST NOT be null nor empty"));
        try {
            SubjectAlternativeNameType.valueOf(type);
        }catch (IllegalArgumentException exception){
            violations.add(new Violation(path, "type", ViolationType.ENUM,
                    String.format("type MUST be one of [%s]", Arrays.toString(
                            SubjectAlternativeNameType.values()))));
        }
        return violations;
    }

    public SubjectAlternativeName toAlternativeName(){
        SubjectAlternativeNameType nameType = SubjectAlternativeNameType.valueOf(type);
        return new SubjectAlternativeName(nameType,
                value);
    }

}
