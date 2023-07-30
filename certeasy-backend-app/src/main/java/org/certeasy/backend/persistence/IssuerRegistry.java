package org.certeasy.backend.persistence;

import org.certeasy.Certificate;
import org.certeasy.backend.issuer.CertIssuer;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface IssuerRegistry {
    Collection<CertIssuer> list() throws IssuerRegistryException;
    CertIssuer add(Certificate certificate) throws IssuerRegistryException;
    boolean exists(String issuerId) throws IssuerRegistryException;
    Optional<CertIssuer> getById(String issuerId) throws IssuerRegistryException;
    void delete(CertIssuer issuer) throws IssuerRegistryException;
    Set<CertIssuer> getChildrenOf(CertIssuer issuer) throws IssuerRegistryException;
    int countChildrenOf(CertIssuer issuer) throws IssuerRegistryException;

}
