package org.certeasy.backend.issuer.rest;

import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.certeasy.backend.issuer.IssuerCertInfo;

import java.util.HashSet;
import java.util.Set;

public record NewIssuerCert (IssuerCertInfo spec, IssuerCertPEM pem, IssuerCertRef ref) implements Validable {

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if(spec==null && pem==null && ref==null) {
            violations.add(new Violation(path, "spec", ViolationType.REQUIRED, "spec is required when neither pem nor ref are provided"));
            violations.add(new Violation(path, "pem", ViolationType.REQUIRED, "pem is required when neither spec nor ref are provided"));
            violations.add(new Violation(path, "ref", ViolationType.REQUIRED, "ref is required when neither pem nor spec are provided"));
            return violations;
        }else if(spec!=null && pem!=null) {
            violations.add(new Violation(path, "spec", ViolationType.PRESENCE, "spec and pem cannot be provided together"));
            violations.add(new Violation(path, "pem", ViolationType.PRESENCE, "spec and pem cannot be provided together"));
            return violations;
        }else if (spec!=null && ref!=null) {
            violations.add(new Violation(path, "spec", ViolationType.PRESENCE, "spec and ref cannot be provided together"));
            violations.add(new Violation(path, "ref", ViolationType.PRESENCE, "spec and ref cannot be provided together"));
            return violations;
        }else if(pem!=null && ref!=null) {
            violations.add(new Violation(path, "pem", ViolationType.PRESENCE, "pem and ref cannot be provided together"));
            violations.add(new Violation(path, "ref", ViolationType.PRESENCE, "pem and ref cannot be provided together"));
            return violations;
        }
        if(spec!=null) {
            violations.addAll(spec.validate(path.append("spec")));
        }
        if(ref!=null) {
            violations.addAll(ref.validate(path.append("ref")));
        }
        if(pem!=null) {
            violations.addAll(pem.validate(path.append("pem")));
        }
        return violations;
    }

}
