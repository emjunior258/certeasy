package org.certeasy;

import java.security.PrivateKey;
import java.util.Set;

/**
 * Encodes/decodes {@link Certificate}s and PrivateKeys to/from PEM.
 */
public interface PEMCoder {
    Certificate decodeCertificate(String cert, String privateKey) throws PEMCoderException;
    String encodePrivateKey(Certificate certificate) throws PEMCoderException;
    String encodeCert(Certificate certificate) throws PEMCoderException;
    String encodeChain(Set<Certificate> certificateSet) throws PEMCoderException;

}
