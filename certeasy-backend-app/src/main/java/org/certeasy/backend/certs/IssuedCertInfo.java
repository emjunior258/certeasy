package org.certeasy.backend.certs;

public record IssuedCertInfo(String name, String serial, boolean ca) implements Comparable<IssuedCertInfo> {

    @Override
    public int compareTo(IssuedCertInfo o) {
        return name.compareTo(o.name);
    }
}
