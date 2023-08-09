package org.certeasy.backend.certs;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record IssuedCert(String serial) {

}
