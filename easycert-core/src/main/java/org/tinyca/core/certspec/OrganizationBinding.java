package org.tinyca.core.certspec;

import org.tinyca.core.RDNConvertible;
import org.tinyca.core.RelativeDistinguishedName;
import org.tinyca.core.SubjectAttributeType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record OrganizationBinding(String organizationName, String department, String title) implements RDNConvertible {

    public OrganizationBinding(String organizationName,  String department, String title) {
        if(organizationName==null || organizationName.isEmpty())
            throw new IllegalArgumentException("organization commonName MUST not be null nor empty");
        this.organizationName = organizationName;
        this.title = title;
        this.department = department;
    }

    @Override
    public Set<RelativeDistinguishedName> toRdns() {
        Set<RelativeDistinguishedName> rdns = new HashSet<>();
        rdns.add(new RelativeDistinguishedName(SubjectAttributeType.OrganizationName,this.organizationName));
        if(department!=null && !department.isEmpty())
            rdns.add(new RelativeDistinguishedName(SubjectAttributeType.OrganizationUnit,
                    department));
        if(title!=null && !title.isEmpty())
            rdns.add(new RelativeDistinguishedName(SubjectAttributeType.Title, title));
        return Collections.unmodifiableSet(rdns);
    }


}
