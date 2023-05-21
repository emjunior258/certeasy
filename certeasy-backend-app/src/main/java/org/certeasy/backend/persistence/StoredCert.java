package org.certeasy.backend.persistence;

import org.certeasy.Certificate;

public interface StoredCert {
    Certificate getCertificate() throws IssuerDatastoreException;
    String getKeyPem() throws IssuerDatastoreException;;
    String getCertPem() throws IssuerDatastoreException;;
}
