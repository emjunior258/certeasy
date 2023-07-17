package org.certeasy.backend.persistence;

import org.certeasy.Certificate;

public interface StoredCert extends Comparable<StoredCert> {
    Certificate getCertificate() throws IssuerDatastoreException;
    String getKeyPem() throws IssuerDatastoreException;
    String getCertPem() throws IssuerDatastoreException;

    default int compareTo(StoredCert cert){
        Certificate certificate = getCertificate();
        return certificate.compareTo(cert.getCertificate());
    }
}
