package org.certeasy.backend.persistence;

import org.certeasy.Certificate;
import org.certeasy.backend.issuer.CertIssuer;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractIssuerRegistry implements IssuerRegistry {

    private final Map<String, Set<CertIssuer>> childrenMap = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger(AbstractIssuerRegistry.class);


    protected void discoverHierarchy(){

        Collection<CertIssuer> certIssuers = this.list();
        Collection<Certificate> certificates = certIssuers.stream()
                .filter(CertIssuer::hasCertificate)
                .map(CertIssuer::getCertificate)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());

        certificates.forEach(cert -> this.childrenMap.put(cert.getIssuerName().digest(),
                    new HashSet<>()));

        this.list().forEach(this::updateHierarchy);

    }

    protected void updateHierarchy(CertIssuer issuer){
        Optional<Certificate> optionalCertificate = issuer.getCertificate();
        if(optionalCertificate.isEmpty())
            return;
        Certificate certificate = optionalCertificate.get();
        if(certificate.isCA() && !childrenMap.containsKey(issuer.getId()))
            childrenMap.put(issuer.getId(), new HashSet<>());
        if(!certificate.isSelfSignedCA()){
            String parentId = certificate.getIssuerName().digest();
            getChildrenSet(parentId).ifPresent(siblings -> {
                LOGGER.debug(String.format("Mapping %s as parent of %s",
                        certificate.getIssuerName().getCommonName(), certificate.getDistinguishedName().getCommonName()));
                siblings.add(issuer);
            });
        }
    }

    protected Optional<Set<CertIssuer>> getChildrenSet(String issuerId){
        if(issuerId==null)
            throw new IllegalArgumentException("issuerId MUST not be null");
        Set<CertIssuer> childrenSet = childrenMap.get(issuerId);
        if(childrenSet==null)
            return Optional.empty();
        return Optional.of(childrenSet);
    }

    @Override
    public Set<CertIssuer> getChildrenOf(CertIssuer issuer) throws IssuerRegistryException {
        if(issuer==null)
            throw new IllegalArgumentException("issuer MUST not be null");
        Optional<Set<CertIssuer>> optionalChildren = getChildrenSet(issuer.getId());
        Set<CertIssuer> childrenSet = optionalChildren.orElse(Collections.emptySet());
        return Collections.unmodifiableSet(childrenSet);
    }

    @Override
    public int countChildrenOf(CertIssuer issuer) throws IssuerRegistryException {
        if(issuer==null)
            throw new IllegalArgumentException("issuer MUST not be null");
        Optional<Set<CertIssuer>> optionalChildren = getChildrenSet(issuer.getId());
        return optionalChildren.map(Set::size).orElse(0);
    }

    public void delete(CertIssuer issuer) throws IssuerRegistryException {
        if(issuer==null)
            throw new IllegalArgumentException("issuerId MUST not be null");
        Optional<Certificate> optionalCert = issuer.getCertificate();
        optionalCert.flatMap(cert -> getChildrenSet(cert.getIssuerName().digest()))
                .ifPresent(set -> set.remove(issuer));
    }


    protected void emptyChildrenMap() {
        this.childrenMap.clear();
    }


}
