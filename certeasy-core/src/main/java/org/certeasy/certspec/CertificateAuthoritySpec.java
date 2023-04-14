package org.certeasy.certspec;

import org.certeasy.DateRange;
import org.certeasy.KeyUsage;
import org.certeasy.CertificateSpec;
import org.certeasy.KeyStrength;

import java.time.LocalDate;
import java.util.Set;

public class CertificateAuthoritySpec extends CertificateSpec {

    public CertificateAuthoritySpec(CertificateAuthoritySubject subject, KeyStrength keyStrength, DateRange validityPeriod) {
        super(subject, keyStrength, validityPeriod, true, Set.of(KeyUsage.CertificateSign,
                KeyUsage.SignCRL), null);
    }

}
