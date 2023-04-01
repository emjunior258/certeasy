package org.certeasy;

/**
 * Restricts the operations for which the  {@link Certificate} public key is intended to be used.
 * More information:
 *  - https://help.hcltechsw.com/domino/10.0.1/conf_keyusageextensionsandextendedkeyusage_r.html
 *  - https://docs.aws.amazon.com/acm/latest/APIReference/API_KeyUsage.html
 */
public enum KeyUsage {


    /**
     * Use when the public key is used with a digital signature mechanism to support security services other than non-repudiation, certificate signing, or CRL signing. A digital signature is often used for entity authentication and data origin authentication with integrity.
     */
    DigitalSignature,


    /**
     * Use when a certificate will be used with a protocol that encrypts keys. An example is S/MIME enveloping, where a fast (symmetric) key is encrypted with the public key from the certificate. SSL protocol also performs key encipherment.
     */
    KeyEncipherment,


    /**
     * Use when the public key is used for encrypting user data, other than cryptographic keys.
     */
    DataEncipherment,


    /**
     * Use when the sender and receiver of the public key need to derive the key without using encryption. This key can then can be used to encrypt messages between the sender and receiver. Key agreement is typically used with Diffie-Hellman ciphers.
     */
    KeyAgreement,


    /**
     * Use only when key agreement is also enabled. This enables the public key to be used only for deciphering data while performing key agreement.
     */
    DecipherOnly,

    /**
     * Use only when key agreement is also enabled. This enables the public key to be used only for enciphering data while performing key agreement.
     */
    EncipherOnly,


    /**
     *
     * Use when the subject public key is used to verify a signature on certificates. This extension can be used only in CA certificates.
     */
    CertificateSign,


    /**
     * Use when the subject public key is to verify a signature on revocation information, such as a CRL.
     */
    SignCRL,

    /**
     * Use when the public key is used to verify digital signatures used to provide a non-repudiation service. Non-repudiation protects against the signing entity falsely denying some action (excluding certificate or CRL signing).
     */
    NonRepudiation,


}
