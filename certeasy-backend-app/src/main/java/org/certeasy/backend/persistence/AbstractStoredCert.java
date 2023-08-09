package org.certeasy.backend.persistence;

import org.certeasy.backend.certs.IssuedCertType;

public abstract class AbstractStoredCert implements StoredCert {

    @Override
    public IssuedCertType getCertType() throws IssuerDatastoreException {
        return IssuedCertType.which(getCertificate());
    }

}
