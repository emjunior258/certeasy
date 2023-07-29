package org.certeasy.backend.issuer;

import org.certeasy.CertEasyException;
import org.certeasy.Certificate;
import org.certeasy.DistinguishedName;
import org.certeasy.backend.persistence.IssuerRegistry;

import java.util.Optional;

public final class IssuerInfoFactory {

    private IssuerInfoFactory(){

    }

    public static IssuerInfo create(CertIssuer issuer, IssuerRegistry registry){
        Optional<Certificate> optionalCertificate = issuer.getCertificate();
        if(optionalCertificate.isEmpty())
            throw new CertEasyException("issuer doesn't have a certificate");
        Certificate certificate = optionalCertificate.get();
        String id = issuer.getId();
        String serial = certificate.getSerial();
        IssuerType issuerType = certificate.isSelfSignedCA() ? IssuerType.ROOT : IssuerType.SUB_CA;
        String distinguishedName = certificate.getDistinguishedName().toString();

        DistinguishedName issuerName = certificate.getIssuerName();
        String parentId = null;
        if(!issuerName.equals(certificate.getDistinguishedName()))
            parentId = issuerName.digest();
        int totalChildren  = registry.countChildrenOf(issuer);
        return new IssuerInfo(id, certificate.getDistinguishedName().getCommonName(),  serial, issuerType,
                distinguishedName, certificate.getBasicConstraints()
                .pathLength(), parentId, totalChildren);

    }

}
