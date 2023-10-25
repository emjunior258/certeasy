package org.certeasy.backend.certs;

import org.certeasy.backend.common.BaseCertSpec;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Set;

public class ServerSpec extends BaseCertSpec {

    private String name;

    private Set<String> domains;

    private String organization;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getDomains() {
        return domains;
    }

    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violationSet = super.validate(path);
        if(name==null || name.isEmpty())
            violationSet.add(new Violation(path, "name", ViolationType.REQUIRED, "must specify a name for the certificate"));
        if(domains==null || domains.isEmpty())
            violationSet.add(new Violation(path, "domains", ViolationType.REQUIRED, "must specify at least one domain"));
        if(domains!=null && !domains.isEmpty()){
            int index=0;
            for(String domain: domains) {
                if(!DomainNameValidator.isValidDomain(domain)){
                    violationSet.add(new Violation(path, String.format("domains[%d]", index),
                            ViolationType.PATTERN, "must match domain name regex pattern"));
                }
                index++;
            }
        }
        if(organization !=null && ( organization.isBlank() || organization.trim().length() < 1) )
            violationSet.add(new Violation(path, "organization", ViolationType.LENGTH, "must have at least a single character"));
        return violationSet;
    }

}
