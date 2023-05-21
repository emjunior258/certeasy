package org.certeasy;

import java.util.Optional;
import java.util.Set;


/**
 * A Definition of a desired {@link Certificate}.
 */
public class CertificateSpec {

    private BasicConstraints basicConstraints;

    private CertificateSubject subject;

    private DateRange validityPeriod;

    private KeyStrength keyStrength;

    private Set<KeyUsage> keyUsages;

    private ExtendedKeyUsages extendedKeyUsages;

    public CertificateSpec(CertificateSubject subject, KeyStrength keyStrength, DateRange validityPeriod, BasicConstraints basicConstraints, Set<KeyUsage> keyUsages, ExtendedKeyUsages extendedKeyUsages){
        if(subject==null)
            throw new IllegalArgumentException("subject MUST not be null");
        if(keyStrength==null)
            throw new IllegalArgumentException("key strength MUST not be null");
        if(validityPeriod ==null)
            throw new IllegalArgumentException("validity MUST not be null");
        if(keyUsages==null || keyUsages.isEmpty())
            throw new IllegalArgumentException("key usages SET must NOT be null nor empty");
        if(basicConstraints==null)
            throw new IllegalArgumentException("basicConstraints MUST not be null");
        this.subject = subject;
        this.keyStrength = keyStrength;
        this.validityPeriod = validityPeriod;
        this.keyUsages = keyUsages;
        this.extendedKeyUsages = extendedKeyUsages;
        this.basicConstraints = basicConstraints;
    }

    public CertificateSubject getSubject() {
        return subject;
    }

    public DateRange getValidityPeriod() {
        return validityPeriod;
    }

    public Set<KeyUsage> getKeyUsages() {
        return keyUsages;
    }

    public KeyStrength getKeyStrength() {
        return keyStrength;
    }

    public BasicConstraints getBasicConstraints() {
        return basicConstraints;
    }

    public Optional<ExtendedKeyUsages> getExtendedKeyUsages() {
        return Optional.ofNullable(this.extendedKeyUsages);
    }

}
