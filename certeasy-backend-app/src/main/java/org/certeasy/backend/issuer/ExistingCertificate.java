package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonGetter;

public class ExistingCertificate {

    private String certPem;

    private String privateKeyPem;

    @JsonGetter("cert_pem")
    public String getCertPem() {
        return certPem;
    }

    @JsonGetter("private_key_pem")
    public String getPrivateKeyPem() {
        return privateKeyPem;
    }
}
