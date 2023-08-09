package org.certeasy.backend.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.HashSet;
import java.util.Set;

@RegisterForReflection
public record CertPEM(
        @JsonProperty("cert_file")
        String certFile,

        @JsonProperty("key_file")
        String keyFile) implements Validable {

        @Override
        public Set<Violation> validate(ValidationPath path) {
                Set<Violation> violations = new HashSet<>();
                if(certFile == null || certFile.isBlank())
                        violations.add(new Violation(path, "cert_file", ViolationType.REQUIRED, "cert_file must not be be null nor empty"));
                if(keyFile == null || keyFile.isBlank())
                        violations.add(new Violation(path, "key_file", ViolationType.REQUIRED, "key_file must not be be null nor empty"));
                return violations;
        }

}
