package org.certeasy.backend.certs;


import org.certeasy.Certificate;
import org.certeasy.KeyStrength;
import org.certeasy.backend.common.BaseResource;
import org.certeasy.backend.common.CertPEM;
import org.certeasy.backend.common.SubCaSpec;
import org.certeasy.backend.common.cert.CertificateConverter;
import org.certeasy.backend.common.cert.NotFoundProblem;
import org.certeasy.backend.common.problem.Problem;
import org.certeasy.backend.common.problem.ProblemResponse;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.issuer.ReadOnlyCertificateException;
import org.certeasy.backend.persistence.StoredCert;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Path("/api/issuers/{issuerId}/certificates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CertificatesResource extends BaseResource {

    @GET
    public Response list(@PathParam("issuerId") String issuerId){
        return this.checkIssuerExistsThen(issuerId, issuer -> {
            Set<IssuedCertInfo> issuedCertInfoSet =  issuer.listCerts().stream().map(storedCert -> {
                Certificate cert = storedCert.getCertificate();
                return new IssuedCertInfo(cert.getDistinguishedName().getCommonName(),
                        cert.getSerial(),
                        cert.getBasicConstraints().ca());
            }).collect(Collectors.toSet());
            return Response.ok(issuedCertInfoSet)
                    .build();
        });
    }

    @POST
    @Path("/tls-server")
    public Response issueTLSServerCertificate(@PathParam("issuerId") String issuerId, ServerSpec spec){
        return this.checkIssuerExistsThen(issuerId, (issuer) -> {
            //TODO: Implement
            throw new UnsupportedOperationException();
        });
    }

    @POST
    @Path("/sub-ca")
    public Response issueSubCaCertificate(@PathParam("issuerId") String issuerId, SubCaSpec spec){
        return this.checkIssuerExistsThen(issuerId, issuer -> {
            CertificateAuthoritySubject subCaSubject = new CertificateAuthoritySubject(
                    spec.getName(),
                    spec.getGeographicAddressInfo().
                            toGeographicAddress());
            CertificateAuthoritySpec subAuthoritySpec = new CertificateAuthoritySpec(subCaSubject, spec.getPathLength(),
                    KeyStrength.valueOf(spec.getKeyStrength()),
                    spec.getValidity().toDateRange());
            Certificate certificate = issuer.issueCert(subAuthoritySpec);
            return Response.ok(new IssuedCert(certificate.getSerial()))
                    .build();
        });
    }

    @GET
    @Path("/{serial}")
    public Response getCertInfo(@PathParam("issuerId") String issuerId, @PathParam("serial") String serial){
        return this.checkIssuerExistsThen(issuerId, issuer -> checkCertExistsThen(issuer, serial, cert -> Response.ok(
                CertificateConverter.convert(cert.getCertificate()))
                .build()));
    }

    @DELETE
    @Path("/{serial}")
    public Response delete(@PathParam("issuerId") String issuerId, @PathParam("serial") String serial){
        return this.checkIssuerExistsThen(issuerId, issuer -> checkCertExistsThen(issuer, serial, cert -> {
            try {
                issuer.deleteIssuedCert(cert);
                return Response.status(Response.Status.NO_CONTENT).build();
            }catch (ReadOnlyCertificateException ex){
                return ProblemResponse.fromProblem(
                        new Problem("/problems/certificate/read-only",
                                "Certificate Read-only",
                                Response.Status.CONFLICT.getStatusCode(),
                        "Certificate is read-only therefore cannot be deleted"));
            }
        }));
    }

    @GET
    @Path("/{serial}/pem")
    public Response getCertPem(@PathParam("issuerId") String issuerId, @PathParam("serial") String serial){
        return this.checkIssuerExistsThen(issuerId, issuer -> checkCertExistsThen(issuer, serial, cert -> Response.ok(
                new CertPEM(cert.getCertPem(), cert.getKeyPem()))
                .build()));
    }

    @GET
    @Path("/{serial}/der")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCertDER(@PathParam("issuerId") String issuerId, @PathParam("serial") String serial){
        return this.checkIssuerExistsThen(issuerId, issuer -> checkCertExistsThen(issuer, serial, cert -> {
            byte[] encodedBytes = Base64.getEncoder().encode(cert.getCertificate().getDERBytes());
            String encodedContent = new String(encodedBytes, StandardCharsets.UTF_8);
            return Response.ok(encodedContent).build();
        }));
    }

    private Response checkCertExistsThen(CertIssuer issuer, String serial, StoredCertOperation operation) {
        Optional<StoredCert> issuedCertOptional = issuer.getIssuedCert(serial);
        if(issuedCertOptional.isEmpty()){
            return ProblemResponse.fromProblem(new NotFoundProblem(
                    "/problems/certificate/not-found",
                    "Certificate not found",
                    "There is no certificate with matching serial: "+serial));
        }
        return operation.getResponse(issuedCertOptional.get());
    }

}
