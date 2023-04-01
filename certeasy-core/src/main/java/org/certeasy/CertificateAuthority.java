package org.certeasy;

import org.certeasy.certspec.CertificateAuthoritySpec;

import java.util.List;
import java.util.Optional;

/**
 * Issues certificates for entities (such as websites, email addresses, companies, or individual persons) and keeps track of them.
 */
public final class CertificateAuthority {

    private final Certificate authorityCert;
    private final CertificateGenerator generator;
    private final CertificateRepository repository;

    public CertificateAuthority(Certificate authorityCert, CertificateGenerator generator, CertificateRepository repository){
        if(authorityCert==null)
            throw new IllegalArgumentException("authorityCert MUST not be null");
        if(generator==null)
            throw new IllegalArgumentException("generator MUST not be null");
        if(repository ==null)
            throw new IllegalArgumentException("repository MUST not be null");
        this.authorityCert = authorityCert;
        this.generator = generator;
        this.repository = repository;
    }

    public Certificate issueCertificate(CertificateSpec spec){
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null");
        if(spec instanceof CertificateAuthoritySpec)
            throw new IllegalArgumentException("CertificateAuthoritySpec not allowed");
        Certificate certificate = this.generator.generate(spec,authorityCert);
        this.repository.putCert(certificate);
        return certificate;
    }

    public List<CertificateSummary> listIssuedCertificates(){
        return this.repository.listCerts();
    }

    public Optional<Certificate> getIssuedCertificate(String serial){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        return this.repository.getCert(serial);
    }

}
