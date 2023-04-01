package org.certeasy;


/**
 * The type of the {@link SubjectAlternativeName}
 */
public enum SubjectAlternativeNameType {

    /**
     * The {@link SubjectAlternativeName} is a DNS name
     */
    DNS,

    /**
     * The {@link SubjectAlternativeName} is an email address
     */
    EMAIL,

    /**
     * The {@link SubjectAlternativeName} is an IP address
     */
    IP_ADDRESS,


    /**
     * The {@link SubjectAlternativeName} is an arbitrary URI
     */
    URI,

    /**
     * The {@link SubjectAlternativeName} is a directory name
     */
    DIRECTORY_NAME,

    /**
     * The {@link SubjectAlternativeName} is a generic string/value
     */
    OTHER_NAME
}
