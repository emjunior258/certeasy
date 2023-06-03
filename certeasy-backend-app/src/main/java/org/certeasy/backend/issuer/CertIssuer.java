package org.certeasy.backend.issuer;

import org.certeasy.*;
import org.certeasy.backend.persistence.IssuerDatastore;
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
    private IssuerDatastore store;
    private Certificate certificate;
    private CertEasyContext context;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(CertIssuer.class);

    private String serial;

    private boolean disabled = false;

    public CertIssuer(String id, IssuerDatastore store, CertEasyContext context){
        if(id==null || id.isEmpty())
            throw new IllegalArgumentException("id MUST not be null nor empty");
        if(store==null)
            throw new IllegalArgumentException("store MUST not be null");
        if(context==null)
            throw new IllegalArgumentException("context MUST not be null");
        this.id = id;
        this.store = store;
        this.context = context;
        this.readSerial();
    }

    public CertIssuer(String id, IssuerDatastore store, CertEasyContext context, Certificate certificate){
        if(id ==null || id.isEmpty())
            throw new IllegalArgumentException("id MUST not be null nor empty");
        if(store==null)
            throw new IllegalArgumentException("store MUST not be null");
        if(context==null)
            throw new IllegalArgumentException("context MUST not be null");
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        this.id = id;
        this.store = store;
        this.context = context;
        this.certificate = certificate;
        this.setCertificate(certificate);

    }

    public String getId() {
        return id;
    }

    private void setCertificate(Certificate certificate){
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        this.store.put(certificate);
        this.certificate = certificate;
        this.serial = certificate.getSerial();
        this.writeSerial();
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean hasCertificate() {
        return serial!=null;
    }

    public Optional<Certificate> getCertificate() {
        if(!hasCertificate())
            return Optional.empty();
        if(certificate!=null)
            return Optional.of(certificate);
        Optional<StoredCert> storedCert = store.getCert(serial);
        storedCert.ifPresent(cert -> this.certificate = cert.getCertificate());
        return Optional.ofNullable(certificate);
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

    public Certificate issueCert(CertificateSpec spec) {
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null");
        if(disabled)
            throw new IllegalStateException("issuerId has been permanently disabled");
        if(!hasCertificate())
            throw new IllegalStateException("issuerId doesn't have a certificate to sign with");
        if(certificate.getBasicConstraints().ca())
            throw new IllegalStateException("issuerId certificate is not CA");
        Certificate issuedCert = context.generator().generate(spec, certificate);
        LOGGER.info("Issued certificate with serial: {}", issuedCert.getSerial());
        this.store.put(issuedCert);
        return certificate;
    }

    public void disable(){
        this.disabled = true;
        this.store.purge();
        this.serial = null;
    }

}
