package org.certeasy.backend.certs;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CertificateSummaryInfo implements Comparable<CertificateSummaryInfo> {
    private String name, serial;
    private IssuedCertType type;

    public CertificateSummaryInfo(String name, String serial, IssuedCertType type){
        this.name = name;
        this.serial = serial;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public IssuedCertType getType() {
        return type;
    }

    public void setType(IssuedCertType type) {
        this.type = type;
    }

    @Override
    public int compareTo(CertificateSummaryInfo o) {
        return name.compareTo(o.name);
    }
}
