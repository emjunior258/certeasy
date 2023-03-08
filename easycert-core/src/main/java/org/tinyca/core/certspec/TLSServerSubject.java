package org.tinyca.core.certspec;

import org.tinyca.core.*;

import java.util.Set;
import java.util.stream.Collectors;

public final class TLSServerSubject extends CertificateSubject {

    public TLSServerSubject(Set<String> domains, GeographicAddress address, String organizationName){
        if(domains==null || domains.isEmpty())
            throw new IllegalArgumentException("domains MUST not be null nor empty");
        if(address==null)
            throw new IllegalArgumentException("address MUST not be null");
        Set<SubjectAlternativeName> sans = domains.stream().map(email -> new SubjectAlternativeName(SubjectAlternativeNameType.EMAIL, email))
                .collect(Collectors.toSet());
        DistinguishedName.Builder dnBuilder =  DistinguishedName.builder();
        dnBuilder.append(new RelativeDistinguishedName(SubjectAttributeType.CommonName,
                domains.iterator().next()));
        if(organizationName!=null && !organizationName.isEmpty())
            dnBuilder.append(new RelativeDistinguishedName(SubjectAttributeType.OrganizationUnit,
                    organizationName));
        this.setDistinguishedName(dnBuilder.build());
        this.setAlternativeNames(sans);
    }


}
