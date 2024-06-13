package org.certeasy.backend.certs;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class IssuedCert {

    private String serial;

    public IssuedCert(String serial){
        this.serial = serial;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

}
