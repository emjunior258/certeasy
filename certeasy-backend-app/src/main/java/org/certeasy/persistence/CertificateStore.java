package org.certeasy.persistence;

import org.certeasy.Certificate;

import java.util.Collection;
import java.util.Optional;

public interface CertificateStore {
    void store(Certificate certificate) throws CertificateStoreException;

    Collection<StoredCert> listStored() throws CertificateStoreException;

    Optional<StoredCert> getCert(String serial) throws CertificateStoreException;

    void deleteCert(String serial) throws CertificateStoreException;
}
