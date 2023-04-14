package org.certeasy;

public class CertEasyException extends RuntimeException {

    public CertEasyException(String message){
        super(message);
    }

    public CertEasyException(String message, Throwable throwable){
        super(message, throwable);
    }

}
