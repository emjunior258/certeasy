package org.certeasy.backend.issuer;

public class ReadOnlyCertificateException extends RuntimeException {

    public ReadOnlyCertificateException(String serial){
        super("Cannot delete a ReadOnly certificate: "+serial);
    }

}
