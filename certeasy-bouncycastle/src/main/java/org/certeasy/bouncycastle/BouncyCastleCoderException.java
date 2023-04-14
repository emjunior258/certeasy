package org.certeasy.bouncycastle;

import org.certeasy.PEMCoderException;

public class BouncyCastleCoderException extends PEMCoderException {

    public BouncyCastleCoderException(String message) {
        super(message);
    }

    public BouncyCastleCoderException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
