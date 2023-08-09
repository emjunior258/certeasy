package org.certeasy.backend.certs;

import org.certeasy.Certificate;
import org.certeasy.DistinguishedName;
import org.certeasy.ExtendedKeyUsages;
import org.certeasy.backend.common.cert.BasicConstraintsInfo;
import org.certeasy.backend.common.cert.SubjectAltNameInfo;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CertificateConverter {

    public static CertificateSummaryInfo toSummaryInfo(Certificate certificate){
        return new CertificateSummaryInfo(certificate.getDistinguishedName().getCommonName(),
                certificate.getSerial(),
                IssuedCertType.which(certificate));
    }

    public static CertificateDetailsInfo toDetailsInfo(Certificate certificate) {
        DistinguishedName distinguishedName = certificate.getDistinguishedName();
        Set<SubjectAltNameInfo> subjectAltNameInfos = certificate.getSubjectAltNames()
                .stream().map(SubjectAltNameInfo::new)
                .collect(Collectors.toSet());
        Optional<ExtendedKeyUsages> extendedKeyUsages = certificate.getExtendedKeyUsages();
        return new CertificateDetailsInfo(
                distinguishedName.getCommonName(),
                certificate.getKeyStrength().getSize(),
                distinguishedName.toString(),
                new BasicConstraintsInfo(certificate.getBasicConstraints()),
                subjectAltNameInfos,
                certificate.getKeyUsages(),
                extendedKeyUsages.orElse(null));
    }

}
