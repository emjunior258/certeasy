package org.certeasy;

public class TinyCaException extends RuntimeException {

    public TinyCaException(String message){
        super(message);
    }

    public TinyCaException(String message, Throwable throwable){
        super(message, throwable);
    }

}
