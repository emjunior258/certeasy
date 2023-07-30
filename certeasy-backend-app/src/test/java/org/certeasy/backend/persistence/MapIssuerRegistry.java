package org.certeasy.backend.persistence;

import org.certeasy.CertEasyContext;
import org.certeasy.Certificate;
import org.certeasy.backend.issuer.CertIssuer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Alternative
@ApplicationScoped
public class MapIssuerRegistry extends AbstractIssuerRegistry implements IssuerRegistry {

    private Map<String, CertIssuer> issuerMap = new HashMap<>();

    private CertEasyContext context;

    public MapIssuerRegistry(CertEasyContext context){
        this.context = context;
    }

    @Override
    public Collection<CertIssuer> list() throws IssuerRegistryException {
        return issuerMap.values();
    }

    @Override
    public CertIssuer add(Certificate certificate) throws IssuerRegistryException {
        CertIssuer issuer = new CertIssuer(this, new MapIssuerDatastore(context), context, certificate);
        if(issuerMap.containsKey(issuer.getId()))
            throw new IssuerDuplicationException(certificate.getDistinguishedName());
        this.issuerMap.put(issuer.getId(), issuer);
        this.updateHierarchy(issuer);
        return issuer;
    }

    @Override
    public boolean exists(String issuerId) throws IssuerRegistryException {
        return issuerMap.containsKey(issuerId);
    }

    @Override
    public Optional<CertIssuer> getById(String issuerId) throws IssuerRegistryException {
        return Optional.ofNullable(this.issuerMap.get(issuerId));
    }

    @Override
    public void delete(CertIssuer issuer) throws IssuerRegistryException {
        this.issuerMap.remove(issuer.getId());
        super.delete(issuer);
    }

    public void clear(){
        this.issuerMap.clear();
    }

}
