package org.tinyca.core;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;


/**
 * A Definition of a desired {@link Certificate}.
 */
public class CertificateSpec {

    private CertificateSubject subject;

    private LocalDate expiryDate;

    private KeyStrength keyStrength;

    private Set<KeyUsage> keyUsages;

    private boolean certificateAuthority = false;

    private ExtendedKeyUsageDefinition extendedKeyUsage;

    public CertificateSpec(CertificateSubject subject, KeyStrength keyStrength, LocalDate expiryDate, Set<KeyUsage> keyUsages, ExtendedKeyUsageDefinition extendedKeyUsage, boolean certificateAuthority){
        if(subject==null)
            throw new IllegalArgumentException("subject MUST not be null");
        if(keyStrength==null)
            throw new IllegalArgumentException("key strength MUST not be null");
        if(expiryDate==null)
            throw new IllegalArgumentException("expiry date MUST not be null");
        if(keyUsages==null || keyUsages.isEmpty())
            throw new IllegalArgumentException("key usages SET must NOT be null nor empty");
        this.subject = subject;
        this.keyStrength = keyStrength;
        this.expiryDate = expiryDate;
        this.keyUsages = keyUsages;
        this.extendedKeyUsage = extendedKeyUsage;
        this.certificateAuthority = certificateAuthority;
    }

    public CertificateSubject getSubject() {
        return subject;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Set<KeyUsage> getPublicKeyUsages() {
        return keyUsages;
    }

    public KeyStrength getKeyStrength() {
        return keyStrength;
    }

    public boolean isCertificateAuthority() {
        return certificateAuthority;
    }

    public Optional<ExtendedKeyUsageDefinition> getExtendedKeyUsage() {
        return Optional.ofNullable(this.extendedKeyUsage);
    }

}
