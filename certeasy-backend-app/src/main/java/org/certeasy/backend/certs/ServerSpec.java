package org.certeasy.backend.certs;

import org.certeasy.backend.common.BaseCertSpec;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Set;

public class ServerSpec extends BaseCertSpec {

    private Set<String> domains;

    public Set<String> getDomains() {
        return domains;
    }

    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violationSet = super.validate(path);
        if(domains==null || domains.isEmpty())
            violationSet.add(new Violation(path, "domains", ViolationType.REQUIRED, "must specify at least one domain"));
        return violationSet;
    }

}
