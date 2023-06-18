package org.certeasy.certspec;

import org.certeasy.RDNConvertible;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record OrganizationBinding(String organizationName, String department, String title) implements RDNConvertible {

    public OrganizationBinding{
        if(organizationName==null || organizationName.isEmpty())
            throw new IllegalArgumentException("organization commonName MUST not be null nor empty");
    }

    @Override
    public Set<RelativeDistinguishedName> toRdns() {
        Set<RelativeDistinguishedName> rdns = new HashSet<>();
        rdns.add(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_NAME,this.organizationName));
        if(department!=null && !department.isEmpty())
            rdns.add(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_UNIT,
                    department));
        if(title!=null && !title.isEmpty())
            rdns.add(new RelativeDistinguishedName(SubjectAttributeType.TITLE, title));
        return Collections.unmodifiableSet(rdns);
    }


}
