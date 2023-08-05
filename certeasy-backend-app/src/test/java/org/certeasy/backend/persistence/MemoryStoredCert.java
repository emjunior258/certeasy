package org.certeasy.backend.persistence;

import org.certeasy.Certificate;

public class MemoryStoredCert implements StoredCert {


    private Certificate certificate;
    private String certPem;
    private String keyPem;

    public MemoryStoredCert(Certificate certificate, String certPem,  String keyPem){
        this.certificate = certificate;
        this.certPem = certPem;
        this.keyPem = keyPem;
    }


    @Override
    public Certificate getCertificate() throws IssuerDatastoreException {
        return this.certificate;
    }

    @Override
    public String getKeyPem() throws IssuerDatastoreException {
        return this.keyPem;
    }

    @Override
    public String getCertPem() throws IssuerDatastoreException {
        return this.certPem;
    }

}
