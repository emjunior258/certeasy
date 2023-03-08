package org.tinyca.core.certspec;

import org.tinyca.core.*;

import java.time.LocalDate;
import java.util.Set;

public class TLSServerCertificateSpec extends CertificateSpec {

    public TLSServerCertificateSpec(TLSServerSubject subject, KeyStrength keyStrength, LocalDate expiryDate) {
        super(subject, keyStrength, expiryDate, Set.of(KeyUsage.DigitalSignature, KeyUsage.KeyEncipherment),
                new ExtendedKeyUsageDefinition(Set.of(ExtendedKeyUsage.TLS_WEB_SERVER_AUTH)),
                false);
    }


}
