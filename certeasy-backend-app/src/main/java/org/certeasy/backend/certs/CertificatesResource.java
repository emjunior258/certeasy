package org.certeasy.backend.certs;


import org.certeasy.Certificate;
import org.certeasy.KeyStrength;
import org.certeasy.backend.common.BaseResource;
import org.certeasy.backend.common.CertPEM;
import org.certeasy.backend.common.OrganizationInfo;
import org.certeasy.backend.common.SubCaSpec;
import org.certeasy.backend.common.cert.NotFoundProblem;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.problem.Problem;
import org.certeasy.backend.common.problem.ProblemResponse;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.issuer.ReadOnlyCertificateException;
import org.certeasy.backend.persistence.StoredCert;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.certeasy.certspec.TLSServerCertificateSpec;
import org.certeasy.certspec.TLSServerSubject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Path("/api/issuers/{issuerId}/certificates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CertificatesResource extends BaseResource {

    @GET
    public Response list(@PathParam("issuerId") String issuerId, @QueryParam("type") String type){
        IssuedCertType issuedCertType = null;
        if(type !=null && !type.isEmpty()){
           try {
               issuedCertType = IssuedCertType.valueOf(type);
           }catch (IllegalArgumentException ex){
               return Response.status(422).entity(new ConstraintViolationProblem(
                       new Violation("query.type", ViolationType.ENUM, "type MUST be one of: " + Arrays.toString(IssuedCertType.values()))))
                       .build();
           }
        }
        IssuedCertType finalIssuedCertType = issuedCertType;
        return this.checkIssuerExistsThen(issuerId, issuer -> {
            Set<CertificateSummaryInfo> certificateSummaryInfoSet =  issuer.listCerts(finalIssuedCertType).stream().map(storedCert -> {
                Certificate cert = storedCert.getCertificate();
                return CertificateConverter.toSummaryInfo(cert);
            }).collect(Collectors.toSet());
            return Response.ok(certificateSummaryInfoSet)
                    .build();
        });
    }

    @POST
    @Path("/tls-server")
    public Response issueTLSServerCertificate(@PathParam("issuerId") String issuerId, ServerSpec spec){
        Set<Violation> violationSet = spec.validate(ValidationPath.of("body"));
        if(!violationSet.isEmpty())
            return Response.status(422).entity(new ConstraintViolationProblem(violationSet))
                    .build();
        return this.checkIssuerExistsThen(issuerId, (issuer) -> {
            TLSServerSubject subject = new TLSServerSubject(
                    spec.getName(),
                    spec.getDomains(),
                    spec.getGeographicAddressInfo().
                            toGeographicAddress(),
                    spec.getOrganization());
            TLSServerCertificateSpec tlsServerCertificateSpec = new TLSServerCertificateSpec(subject,
                    KeyStrength.valueOf(spec.getKeyStrength()),
                    spec.getValidity().toDateRange());
            Certificate certificate = issuer.issueCert(tlsServerCertificateSpec);
            return Response.ok(new IssuedCert(certificate.getSerial()))
                    .build();

        });
    }

    @POST
    @Path("/sub-ca")
    public Response issueSubCaCertificate(@PathParam("issuerId") String issuerId, SubCaSpec spec){
        Set<Violation> violationSet = spec.validate(ValidationPath.of("body"));
        if(!violationSet.isEmpty())
            return Response.status(422).entity(new ConstraintViolationProblem(violationSet))
                    .build();
        return this.checkIssuerExistsThen(issuerId, issuer -> {
            String organizationName = null;
            String organizationUnit = null;
            OrganizationInfo organizationInfo = spec.getOrganizationInfo();
            if(organizationInfo!= null) {
                organizationName = organizationInfo.organizationName();
                organizationUnit = organizationInfo.organizationUnit();
            }
            CertificateAuthoritySubject subCaSubject = new CertificateAuthoritySubject(
                    spec.getName(),
                    spec.getGeographicAddressInfo().
                            toGeographicAddress(), organizationName, organizationUnit);
            CertificateAuthoritySpec subAuthoritySpec = new CertificateAuthoritySpec(subCaSubject, spec.getPathLength(),
                    KeyStrength.valueOf(spec.getKeyStrength()),
                    spec.getValidity().toDateRange());
            Certificate certificate = issuer.issueCert(subAuthoritySpec);
            return Response.ok(new CreatedSubCa(certificate.getDistinguishedName().digest(), certificate.getSerial()))
                    .build();
        });
    }

    @GET
    @Path("/{serial}")
    public Response getCertInfo(@PathParam("issuerId") String issuerId, @PathParam("serial") String serial){
        return this.checkIssuerExistsThen(issuerId, issuer -> checkCertExistsThen(issuer, serial, cert -> Response.ok(
                CertificateConverter.toDetailsInfo(cert.getCertificate()))
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
