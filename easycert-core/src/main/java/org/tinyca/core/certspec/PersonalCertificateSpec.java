package org.tinyca.core.certspec;

import org.tinyca.core.*;

import java.time.LocalDate;
import java.util.Set;

public class PersonalCertificateSpec extends CertificateSpec {

    public PersonalCertificateSpec(PersonalIdentitySubject subject, KeyStrength keyStrength, LocalDate expiryDate) {
        super(subject, keyStrength, expiryDate, Set.of(KeyUsage.DigitalSignature, KeyUsage.NonRepudiation),
                new ExtendedKeyUsageDefinition(Set.of(ExtendedKeyUsage.TLS_WEB_CLIENT_AUTH, ExtendedKeyUsage.SIGN_CODE,
                        ExtendedKeyUsage.EMAIL_PROTECTION)),
                false);
    }
}
