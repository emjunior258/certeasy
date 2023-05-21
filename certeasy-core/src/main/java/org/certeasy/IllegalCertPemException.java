package org.certeasy;

public class IllegalCertPemException extends IllegalArgumentException {

    public IllegalCertPemException(){
        this("Not a valid PEM encoded certificate");
    }

    public IllegalCertPemException(String message){
        super(message);
    }

}
