package org.certeasy.backend.persistence;

import org.certeasy.Certificate;

import java.util.Collection;
import java.util.Optional;

public interface IssuerDatastore {

    void put(Certificate certificate) throws IssuerDatastoreException;

    Collection<StoredCert> listStored() throws IssuerDatastoreException;

    Optional<StoredCert> getCert(String serial) throws IssuerDatastoreException;

    void deleteCert(String serial) throws IssuerDatastoreException;

    Optional<String> getIssuerCertSerial() throws IssuerDatastoreException;

    void putIssuerCertSerial(String serial) throws IssuerDatastoreException;

    void purge() throws IssuerDatastoreException;

}
