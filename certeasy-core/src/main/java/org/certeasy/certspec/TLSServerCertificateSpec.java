package org.certeasy.certspec;

import org.certeasy.*;

import java.util.Set;

public class TLSServerCertificateSpec extends CertificateSpec {

    public TLSServerCertificateSpec(TLSServerSubject subject, KeyStrength keyStrength, DateRange validityPeriod) {
        super(subject, keyStrength, validityPeriod, new BasicConstraints(false), Set.of(KeyUsage.DIGITAL_SIGNATURE, KeyUsage.KEY_ENCIPHERMENT),
                new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.TLS_WEB_SERVER_AUTH)));
    }

}
