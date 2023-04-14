package org.certeasy.persistence;

import org.certeasy.Certificate;

public interface StoredCert {
    Certificate getCertificate() throws CertificateStoreException;
    String getKeyPem() throws CertificateStoreException;;
    String getCertPem() throws CertificateStoreException;;
}
