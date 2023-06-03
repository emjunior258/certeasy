package org.certeasy.backend.persistence;

import org.certeasy.Certificate;
import org.certeasy.backend.issuer.CertIssuer;

import java.util.Collection;
import java.util.Optional;

public interface IssuerRegistry {
    Collection<CertIssuer> list() throws IssuerRegistryException;
    CertIssuer add(String issuerId, Certificate certificate) throws IssuerRegistryException;
    boolean exists(String name) throws IssuerRegistryException;
    Optional<CertIssuer> getById(String issuerId) throws IssuerRegistryException;
    void delete(CertIssuer issuer) throws IssuerRegistryException;
}
