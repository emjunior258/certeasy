package org.certeasy.backend.certs;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CertificateSummaryInfo(String name, String serial, IssuedCertType type) implements Comparable<CertificateSummaryInfo> {

    @Override
    public int compareTo(CertificateSummaryInfo o) {
        return name.compareTo(o.name);
    }
}
