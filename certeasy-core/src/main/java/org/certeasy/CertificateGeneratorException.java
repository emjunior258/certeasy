package org.certeasy;


/**
 * Anything unexpected on the {@link CertificateGenerator}.
 */
public class CertificateGeneratorException extends CertEasyException {

    public CertificateGeneratorException(String message) {
        super(message);
    }

    public CertificateGeneratorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
