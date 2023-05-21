package org.certeasy;

public interface CertEasyContext {
    CertificateGenerator generator();
    PEMCoder pemCoder();
}
