package org.certeasy;

import java.security.PrivateKey;

public record TestCert(Certificate certificate, byte[] derBytes, PrivateKey privateKey, CertAttributes attributes) {

}
