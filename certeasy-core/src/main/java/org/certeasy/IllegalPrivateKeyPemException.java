package org.certeasy;

public class IllegalPrivateKeyPemException extends IllegalArgumentException {

    public IllegalPrivateKeyPemException(){
        this("Not a valid PEM encoded private key");
    }

    public IllegalPrivateKeyPemException(String message){
        super(message);
    }

}
