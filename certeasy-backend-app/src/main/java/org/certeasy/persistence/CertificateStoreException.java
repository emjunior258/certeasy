package org.certeasy.persistence;

import org.certeasy.CertEasyException;

public class CertificateStoreException extends CertEasyException {

    public CertificateStoreException(String message) {
        super(message);
    }

    public CertificateStoreException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
