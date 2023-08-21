package org.certeasy.certspec;

import org.certeasy.*;

public final class CertificateAuthoritySubject extends CertificateSubject {

    public CertificateAuthoritySubject(String name, GeographicAddress address){
        this(name, address, null, null);
    }

    public CertificateAuthoritySubject(String name, GeographicAddress address, String organizationName, String organizationUnit) {
        if(name==null)
            throw new IllegalArgumentException("ca commonName MUST not be null");
        if(address==null)
            throw new IllegalArgumentException("address MUST not be null");
        DistinguishedName.Builder dnBuilder = DistinguishedName.builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.COMMON_NAME, name))
                .append(address);
        if(organizationName != null && !organizationName.isBlank())
                dnBuilder.append(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_NAME, organizationName));
        if(organizationUnit != null && !organizationUnit.isBlank())
            dnBuilder.append(new RelativeDistinguishedName(SubjectAttributeType.ORGANIZATION_UNIT, organizationUnit));
        this.setDistinguishedName(dnBuilder.build());
    }


}
