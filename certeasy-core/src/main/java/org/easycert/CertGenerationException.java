package org.easycert;


/**
 * Something unexpected during certificate generation
 */
public class CertGenerationException extends TinyCaException {

    public CertGenerationException(String message) {
        super(message);
    }

    public CertGenerationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
