package org.certeasy.bouncycastle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class BouncyCastlePEMCoderTest {

    @Test
    @DisplayName("Should thrown an exception when decode with empty parameters")
    void decodeCertificate() {
        BouncyCastlePEMCoder bouncyCastlePEMCoder = new BouncyCastlePEMCoder();
        Assertions.assertThrows(IllegalArgumentException.class, () -> bouncyCastlePEMCoder.decodeCertificate("", ""));
    }

    /**
    @Test
    void encodePrivateKey() {
    }

    @Test
    void encodeCert() {
    }

    @Test
    void encodeChain() {
    }*/
}