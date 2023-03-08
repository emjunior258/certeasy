package org.easycert.certspec;

import org.easycert.KeyUsage;
import org.easycert.CertificateSpec;
import org.easycert.KeyStrength;

import java.time.LocalDate;
import java.util.Set;

public class CertificateAuthoritySpec extends CertificateSpec {

    public CertificateAuthoritySpec(CertificateAuthoritySubject subject, KeyStrength keyStrength, LocalDate expiryDate) {
        super(subject, keyStrength, expiryDate, Set.of(KeyUsage.CertificateSign,
                KeyUsage.SignCRL), null,true);
    }

}
