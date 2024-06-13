package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;


@RegisterForReflection
public class CustomIssuedCaCert extends IssuedCert {

    @JsonProperty("issuer_id")
    private String issuerId;

    public CustomIssuedCaCert(String serial, String issuerId) {
        super(serial);
        this.issuerId = issuerId;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }
}
