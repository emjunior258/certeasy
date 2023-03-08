package org.tinyca.core;

/**
 * Summarized information about an X509 V3 {@link Certificate}.
 * @param commonName the common name of the subject
 * @param serial the serial of the {@link Certificate}
 * @param type the type of {@link Certificate}
 * @param status the current status of the {@link Certificate}
 */
public record CertificateSummary(String commonName, String serial, CertificateType type, CertificateStatus status) {

    public CertificateSummary(String commonName, String serial, CertificateType type, CertificateStatus status){
        if(commonName ==null|| commonName.isEmpty())
            throw new IllegalArgumentException("commonName MUST not be null nor empty");
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(type==null)
            throw new IllegalArgumentException("type MUST not be null");
        if(status==null)
            throw new IllegalArgumentException("status MUST not be null");
        this.commonName = commonName;
        this.serial = serial;
        this.type = type;
        this.status = status;
    }

}
