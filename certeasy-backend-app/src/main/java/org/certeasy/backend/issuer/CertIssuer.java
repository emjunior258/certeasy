package org.certeasy.backend.issuer;

import org.certeasy.*;
import org.certeasy.backend.persistence.IssuerDatastore;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.backend.persistence.StoredCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;


/**
 * Represents a certificate issuerId: issues certificates and keeps record of them.
 */
public class CertIssuer {

    private String id;
    private IssuerRegistry registry;

    private IssuerDatastore store;
    private Certificate certificate;
    private CertEasyContext context;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(CertIssuer.class);

    private String serial;

    private boolean disabled = false;

    public CertIssuer(IssuerRegistry registry, IssuerDatastore store, CertEasyContext context){
        if(registry==null)
            throw new IllegalArgumentException("registry MUST not be null");
        if(store==null)
            throw new IllegalArgumentException("store MUST not be null");
        if(context==null)
            throw new IllegalArgumentException("context MUST not be null");
        this.registry = registry;
        this.store = store;
        this.context = context;
        this.readSerial();
    }

    public CertIssuer(IssuerRegistry registry, IssuerDatastore store, CertEasyContext context, Certificate certificate){
        if(registry==null)
            throw new IllegalArgumentException("registry MUST not be null");
        if(store==null)
            throw new IllegalArgumentException("store MUST not be null");
        if(context==null)
            throw new IllegalArgumentException("context MUST not be null");
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        this.registry = registry;
        this.store = store;
        this.context = context;
        this.certificate = certificate;
        this.setCertificate(certificate);
        this.figureIdentity();

    }

    public String getId() {
        return id;
    }

    private void setCertificate(Certificate certificate){
        this.store.put(certificate);
        this.certificate = certificate;
        this.serial = certificate.getSerial();
        this.writeSerial();
    }

    private void figureIdentity(){
        this.id = certificate.
                getDistinguishedName().digest();
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean hasCertificate() {
        return serial!=null;
    }

    public Optional<Certificate> getCertificate() {
        this.loadCertIfNotYet();
        return Optional.ofNullable(certificate);
    }

    public void deleteIssuedCert(StoredCert storedCert){
        if(storedCert.getCertificate().getSerial().equals(serial))
            throw new ReadOnlyCertificateException(storedCert.getCertificate().
                    getSerial());
        this.store.deleteCert(storedCert.getCertificate().getSerial());
    }

    private void loadCertIfNotYet() {
        if(certificate!=null)
            return;
        Optional<StoredCert> storedCert = store.getCert(serial);
        storedCert.ifPresent(cert -> {
            this.certificate = cert.getCertificate();
            this.figureIdentity();
        });
    }

    private void writeSerial(){
        this.store.putIssuerCertSerial(serial);
    }

    private void readSerial(){
        this.store.getIssuerCertSerial().ifPresent(it -> this.serial = it);
    }

    public Collection<StoredCert> listCerts() {
        return store.listStored();
    }

    public Optional<StoredCert> getIssuedCert(String serial){
        return this.store.getCert(serial);
    }

    public Certificate issueCert(CertificateSpec spec) {
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null");
        if(disabled)
            throw new IllegalStateException("issuer has been permanently disabled");
        if(!hasCertificate())
            throw new IllegalStateException("issuer doesn't have a certificate to sign with");
        this.loadCertIfNotYet();
        if(!certificate.getBasicConstraints().ca())
            throw new IllegalStateException("issuer certificate is not CA");
        Certificate issuedCert = context.generator().generate(spec, certificate);
        LOGGER.info("Issued certificate with serial: {}", issuedCert.getSerial());
        this.store.put(issuedCert);
        if(spec.getBasicConstraints().ca()) {
            LOGGER.info("Creating issuer for issued certificate: "+certificate.getSerial());
            this.registry.add(issuedCert);
        }
        return issuedCert;
    }

    public void disable(){
        this.disabled = true;
        this.store.purge();
        this.serial = null;
    }

}
