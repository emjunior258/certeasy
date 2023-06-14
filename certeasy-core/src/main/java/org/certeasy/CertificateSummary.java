package org.certeasy;

/**
 * Summarized information about an X509 V3 {@link Certificate}.
 * @param commonName the common name of the subject
 * @param serial the serial of the {@link Certificate}
 * @param validityPeriod the period for which the  {@link Certificate} will remain valid
 */
public record CertificateSummary(String commonName, String serial, DateRange validityPeriod) {

    public CertificateSummary{
        if(commonName ==null|| commonName.isEmpty())
            throw new IllegalArgumentException("commonName MUST not be null nor empty");
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
    }

}
