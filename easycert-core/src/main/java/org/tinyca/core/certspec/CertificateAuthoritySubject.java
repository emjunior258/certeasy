package org.tinyca.core.certspec;

import org.tinyca.core.*;

public final class CertificateAuthoritySubject extends CertificateSubject {

    public CertificateAuthoritySubject(String name, GeographicAddress address) {
        if(name==null)
            throw new IllegalArgumentException("ca commonName MUST not be null");
        if(address==null)
            throw new IllegalArgumentException("address MUST not be null");
        this.setDistinguishedName(DistinguishedName.builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, name))
                .append(address)
                .build()
        );
    }


}
