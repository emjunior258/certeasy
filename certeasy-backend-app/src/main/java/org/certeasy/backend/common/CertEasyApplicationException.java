package org.certeasy.backend.common;

import org.certeasy.CertEasyException;

public class CertEasyApplicationException extends CertEasyException  {

    public CertEasyApplicationException(String message) {
        super(message);
    }

    public CertEasyApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
