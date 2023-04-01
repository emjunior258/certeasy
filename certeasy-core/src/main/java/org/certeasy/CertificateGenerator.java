package org.certeasy;

import org.certeasy.certspec.CertificateAuthoritySpec;

/**
 * Generates {@link Certificate}s from {@link CertificateSpec}s.
 */
public interface CertificateGenerator {

    /**
     * Generates a child {@link Certificate} signed by a CA.
     * @param spec the specification of the {@link Certificate} to be Generated
     * @param authorityCertificate the {@link Certificate} of the signing CA
     * @return the generated and signed {@link Certificate}
     * @throws IllegalArgumentException if the provided spec is null
     * @throws IllegalArgumentException if the provided authorityCertificate is not a CA
     * @throws IllegalArgumentException if the provided authorityCertificate is null
     * @throws CertGenerationException if an internal error occurs while generating the certificate
     */
    Certificate generate(CertificateSpec spec, Certificate authorityCertificate) throws CertGenerationException;


    /**
     * Generates a CA Root Certificate
     * @param spec the specification of the certificate authority
     * @return the Generated CA Root {@link Certificate}
     * @throws IllegalArgumentException if the spec is null
     * @throws CertGenerationException if an internal error occurs while generating the certificate
     */
    Certificate generate(CertificateAuthoritySpec spec) throws CertGenerationException;

}
