package org.certeasy.certspec;

import org.certeasy.*;

import java.util.Set;

public class PersonalCertificateSpec extends CertificateSpec {

    public PersonalCertificateSpec(PersonalIdentitySubject subject, KeyStrength keyStrength, DateRange validityPeriod) {
        super(subject, keyStrength, validityPeriod, new BasicConstraints(false), Set.of(KeyUsage.DIGITAL_SIGNATURE, KeyUsage.NON_REPUDIATION),
                new ExtendedKeyUsages(Set.of(ExtendedKeyUsage.TLS_WEB_CLIENT_AUTH, ExtendedKeyUsage.SIGN_CODE,
                        ExtendedKeyUsage.EMAIL_PROTECTION)));
    }
}
