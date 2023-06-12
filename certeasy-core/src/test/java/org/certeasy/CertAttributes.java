package org.certeasy;

import java.util.Set;

public record CertAttributes(String serial, DistinguishedName subject, Set<SubjectAlternativeName> subjectAltNames, DistinguishedName issuer, KeyStrength keyStrength, DateRange validity, BasicConstraints basicConstraints, Set<KeyUsage> keyUsages, ExtendedKeyUsages extendedKeyUsages) {

}
