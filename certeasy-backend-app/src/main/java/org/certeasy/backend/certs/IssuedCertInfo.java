package org.certeasy.backend.certs;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record IssuedCertInfo(String name, String serial, boolean ca) implements Comparable<IssuedCertInfo> {

    @Override
    public int compareTo(IssuedCertInfo o) {
        return name.compareTo(o.name);
    }
}
