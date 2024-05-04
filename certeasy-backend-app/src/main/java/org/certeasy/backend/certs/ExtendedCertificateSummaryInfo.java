package org.certeasy.backend.certs;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ExtendedCertificateSummaryInfo extends CertificateSummaryInfo {

    @JsonProperty(value = "issuer_id")
    private String issuerId;

    public ExtendedCertificateSummaryInfo(String name, String serial, IssuedCertType type, String  issuerId){
        super(name, serial, type);
        this.issuerId = issuerId;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

}
