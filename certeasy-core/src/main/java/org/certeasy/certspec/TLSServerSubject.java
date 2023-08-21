package org.certeasy.certspec;

import org.certeasy.*;

import java.util.Set;
import java.util.stream.Collectors;

public final class TLSServerSubject extends CertificateSubject {

    public TLSServerSubject(String name, Set<String> domains, GeographicAddress address, String organizationName){
        if(name==null || name.isEmpty())
            throw new IllegalArgumentException("name MUST not be null nor empty");
        if(domains==null || domains.isEmpty())
            throw new IllegalArgumentException("domains MUST not be null nor empty");
        if(address==null)
            throw new IllegalArgumentException("address MUST not be null");
        Set<SubjectAlternativeName> sans = domains.stream().map(domain -> new SubjectAlternativeName(SubjectAlternativeNameType.DNS, domain))
                .collect(Collectors.toSet());
        DistinguishedName.Builder dnBuilder =  DistinguishedName.builder();
        dnBuilder.append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME,
                name));
        if(organizationName!=null && !organizationName.isEmpty())
            dnBuilder.append(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_NAME,
                    organizationName));
        this.setDistinguishedName(dnBuilder.build());
        this.setAlternativeNames(sans);
    }


}
