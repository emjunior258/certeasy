package org.certeasy;

import java.util.List;
import java.util.Optional;

/**
 * Stores {@link Certificate}s issued by a Certificate Authority
 */
public interface CertificateRepository {

    void putCert(Certificate certificate);

    Optional<Certificate> getCert(String serial);

    List<CertificateSummary> listCerts();

}
