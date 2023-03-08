package org.easycert;

/**
 * A second degree of restriction in terms of how the public key of a {@link Certificate} is intended to be used.
 * The extended key usage is not about the operations that can be performed with the key, it is instead about the context in which those operations can be performed.
 *
 * More information:
 *  - https://oidref.com/1.3.6.1.5.5.7.3.2
 *  - https://docs.aws.amazon.com/acm/latest/APIReference/API_ExtendedKeyUsage.html
 */
public enum ExtendedKeyUsage {


    /**
     * Certificate may be used to authenticate a Web server
     */
    TLS_WEB_SERVER_AUTH("1.3.6.1.5.5.7.3.1"),


    /**
     * Certificate may be used to Authenticate a Web client
     */
    TLS_WEB_CLIENT_AUTH("1.3.6.1.5.5.7.3.2"),


    /**
     * Certificate may be used to sign downloadable code
     */
    SIGN_CODE("1.3.6.1.5.5.7.3.3 "),


    /**
     * Certificate may be used to sign and verify emails
     */
    EMAIL_PROTECTION("1.3.6.1.5.5.7.3.4"),

    TIMESTAMPING("1.3.6.1.5.5.7.3.8 "),

    OCSP_SIGNING("1.3.6.1.5.5.7.3.9"),


    /**
     * Certificate may be used by an IPSEC system to encrypt traffic
     */
    IPSEC_END_SYSTEM("1.3.6.1.5.5.7.3.5"),


    /**
     * Certificate may be used to encrypt an IPSEC tunnel
     */
    IPSEC_TUNNEL("1.3.6.1.5.5.7.3.6"),


    /**
     *
     */
    IPSEC_USER("1.3.6.1.5.5.7.3.7");


    private String oid;


    ExtendedKeyUsage(String oid){
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }
}
