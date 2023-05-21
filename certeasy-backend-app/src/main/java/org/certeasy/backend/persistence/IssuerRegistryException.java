package org.certeasy.backend.persistence;

import org.certeasy.CertEasyException;

public class IssuerRegistryException extends CertEasyException {

    public IssuerRegistryException(String message) {
        super(message);
    }

    public IssuerRegistryException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
