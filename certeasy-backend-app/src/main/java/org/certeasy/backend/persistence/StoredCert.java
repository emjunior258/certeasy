package org.certeasy.backend.persistence;

import org.certeasy.Certificate;
import org.certeasy.backend.certs.IssuedCertType;

public interface StoredCert extends Comparable<StoredCert> {
    Certificate getCertificate() throws IssuerDatastoreException;
    String getKeyPem() throws IssuerDatastoreException;
    String getCertPem() throws IssuerDatastoreException;
    IssuedCertType getCertType() throws IssuerDatastoreException;

    default int compareTo(StoredCert cert){
        Certificate certificate = getCertificate();
        return certificate.compareTo(cert.getCertificate());
    }
}
