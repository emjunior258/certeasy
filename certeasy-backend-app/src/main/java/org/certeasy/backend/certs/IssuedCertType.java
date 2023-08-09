package org.certeasy.backend.certs;

import org.certeasy.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public enum IssuedCertType {
    CA, TLS_SERVER, PERSONAL, EMPLOYEE, CUSTOM;

    public static IssuedCertType which(Certificate certificate){
        DistinguishedName distinguishedName = certificate.getDistinguishedName();
        Set<IssuedCertType> matchedTypes = new HashSet<>();
        if(certificate.isCA())
            matchedTypes.add(IssuedCertType.CA);
        if(distinguishedName.hasAttribute(SubjectAttributeType.SURNAME)
                && distinguishedName.hasAttribute(SubjectAttributeType.GIVEN_NAME)
                && !distinguishedName.hasAttribute(SubjectAttributeType.ORGANIZATION_NAME))
            matchedTypes.add(IssuedCertType.PERSONAL);
        if(distinguishedName.hasAttribute(SubjectAttributeType.TITLE)
                && distinguishedName.hasAttribute(SubjectAttributeType.ORGANIZATION_NAME))
            matchedTypes.add(IssuedCertType.EMPLOYEE);
        Optional<ExtendedKeyUsages> optionalExtendedKeyUsages = certificate.getExtendedKeyUsages();
        optionalExtendedKeyUsages.ifPresent(extendedKeyUsages -> {
            if(extendedKeyUsages.usages().contains(ExtendedKeyUsage.TLS_WEB_SERVER_AUTH))
                matchedTypes.add(IssuedCertType.TLS_SERVER);
        });
        if(matchedTypes.size() > 1)
            return IssuedCertType.CUSTOM;
        return matchedTypes.iterator().next();
    }


}
