package org.tinyca.core.certspec;

import org.tinyca.core.CertificateSpec;
import org.tinyca.core.KeyStrength;
import org.tinyca.core.KeyUsage;

import java.time.LocalDate;
import java.util.Set;

public class CertificateAuthoritySpec extends CertificateSpec {

    public CertificateAuthoritySpec(CertificateAuthoritySubject subject, KeyStrength keyStrength, LocalDate expiryDate) {
        super(subject, keyStrength, expiryDate, Set.of(KeyUsage.CertificateSign,
                KeyUsage.SignCRL), null,true);
    }

}
