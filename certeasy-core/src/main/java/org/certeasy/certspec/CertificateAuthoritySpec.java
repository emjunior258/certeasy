package org.certeasy.certspec;

import org.certeasy.*;

import java.util.Set;

public class CertificateAuthoritySpec extends CertificateSpec {

    public CertificateAuthoritySpec(CertificateAuthoritySubject subject, int pathLength, KeyStrength keyStrength, DateRange validityPeriod) {
        super(subject, keyStrength, validityPeriod, new BasicConstraints(true, pathLength), Set.of(KeyUsage.CERTIFICATE_SIGN,
                KeyUsage.SIGN_CRL), null);
    }

}
