package org.certeasy.backend.common.cert;

import org.certeasy.Certificate;
import org.certeasy.DistinguishedName;
import org.certeasy.ExtendedKeyUsages;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CertificateConverter {

    public static CertificateInfo convert(Certificate certificate) {
        DistinguishedName distinguishedName = certificate.getDistinguishedName();
        Set<SubjectAltNameInfo> subjectAltNameInfos = certificate.getSubjectAltNames()
                .stream().map(SubjectAltNameInfo::new)
                .collect(Collectors.toSet());
        Optional<ExtendedKeyUsages> extendedKeyUsages = certificate.getExtendedKeyUsages();
        return new CertificateInfo(
                distinguishedName.getCommonName(),
                certificate.getKeyStrength().getSize(),
                distinguishedName.toString(),
                new BasicConstraintsInfo(certificate.getBasicConstraints()),
                subjectAltNameInfos,
                certificate.getKeyUsages(),
                extendedKeyUsages.orElse(null));
    }

}
