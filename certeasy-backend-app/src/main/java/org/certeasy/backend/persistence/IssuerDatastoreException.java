package org.certeasy.backend.persistence;

import org.certeasy.CertEasyException;

public class IssuerDatastoreException extends CertEasyException {

    public IssuerDatastoreException(String message) {
        super(message);
    }

    public IssuerDatastoreException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
