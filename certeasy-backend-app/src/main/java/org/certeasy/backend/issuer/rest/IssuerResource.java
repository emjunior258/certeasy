package org.certeasy.backend.issuer.rest;

import org.certeasy.*;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.problem.Problem;
import org.certeasy.backend.common.problem.ServerErrorProblem;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.issuer.IssuerCertInfo;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.backend.persistence.StoredCert;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Path("/api/issuers/{issuerId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IssuerResource {
    @Inject
    IssuerRegistry registry;
    @Inject
    CertEasyContext context;
    final String ID_REGEX = "^[a-z]+(-[a-z]+)*$";
    final Pattern ID_PATTERN = Pattern.compile(ID_REGEX);
    private static final Logger LOGGER = Logger.getLogger(IssuerResource.class);

    @DELETE
    public Response deleteIssuer(@PathParam("issuerId") String issuerId){
        Optional<Response> optionalIdResponse = this.checkIssuerId(issuerId, true);
        if(optionalIdResponse.isPresent())
            return optionalIdResponse.get();
        registry.getById(issuerId).ifPresent(registry::delete);
        return Response.noContent().build();
    }

    @POST
    @Path("/cert-spec")
    public Response createFromSpec(@PathParam("issuerId") String id, IssuerCertInfo spec){
        Optional<Response> optionalIdResponse = this.checkIssuerId(id, false);
        if(optionalIdResponse.isPresent())
            return optionalIdResponse.get();
        Set<Violation> violationSet = new HashSet<>(spec.validate(ValidationPath.of("body")));
        if(!violationSet.isEmpty())
            return Response.status(422).entity(new ConstraintViolationProblem(violationSet))
                    .build();
        CertificateAuthoritySubject subject = new CertificateAuthoritySubject(
                spec.name(),
                spec.geographicAddressInfo().
                        toGeographicAddress());
        CertificateAuthoritySpec authoritySpec = new CertificateAuthoritySpec(subject, spec.pathLength(),
                KeyStrength.valueOf(spec.keyStrength()),
                spec.validity().toDateRange());
        Certificate certificate = context.generator().generate(authoritySpec);
        registry.add(id, certificate);
        return Response.noContent().build();
    }

    @POST
    @Path("/cert-pem")
    public Response createFromPem(@PathParam("issuerId") String id, IssuerCertPEM pem){
        Optional<Response> optionalIdResponse = this.checkIssuerId(id, false);
        if(optionalIdResponse.isPresent())
            return optionalIdResponse.get();
        Set<Violation> violationSet = new HashSet<>(pem.validate(ValidationPath.of("body")));
        if(!violationSet.isEmpty())
            return Response.status(422).entity(new ConstraintViolationProblem(violationSet))
                    .build();
        try{
            Certificate certificate = context.pemCoder().decodeCertificate(pem.certFile(),
                    pem.keyFile());
            BasicConstraints basicConstraints = certificate.getBasicConstraints();
            if(basicConstraints==null || !basicConstraints.ca())
                return Response.status(422).entity(new ConstraintViolationProblem(
                        new Violation("body.pem.cert_file", ViolationType.STATE,
                                "not_ca", "cert_file is does not have CA basic constraint"
                        ))).build();
            registry.add(id, certificate);
            return Response.noContent().build();
        }catch (IllegalCertPemException ex) {
            return Response.status(422).entity(new ConstraintViolationProblem(
                    new Violation("body.pem.cert_file", ViolationType.FORMAT,
                            "cert_file is NOT a valid PEM encoded certificate"
                    ))).build();
        }catch (IllegalPrivateKeyPemException ex){
            return Response.status(422).entity(new ConstraintViolationProblem(
                    new Violation("body.pem.key_file", ViolationType.FORMAT,
                            "key_file is NOT a valid PEM encoded private key"
                    ))).build();
        }catch (PEMCoderException ex){
            String message = "Error decoding pem content";
            LOGGER.error(message, ex);
            return Response.status(500).entity(new ServerErrorProblem(message))
                    .build();
        }
    }

    @POST
    @Path("/cert-ref")
    public Response createFromRef(@PathParam("issuerId") String issuerId, IssuerCertRef ref){
        Optional<Response> optionalIdResponse = this.checkIssuerId(issuerId, false);
        if(optionalIdResponse.isPresent())
            return optionalIdResponse.get();
        Set<Violation> violationSet = new HashSet<>();
        violationSet.addAll(ref.validate(ValidationPath.of("body")));
        if(!violationSet.isEmpty())
            return Response.status(422).entity(new ConstraintViolationProblem(violationSet))
                    .build();

        Optional<CertIssuer> optionalCertIssuer = registry.getById(ref.issuerId());
        if(optionalCertIssuer.isEmpty()){
            //TODO: Issuer not resolved
        }
        CertIssuer certIssuer = optionalCertIssuer.get();
        Optional<StoredCert> optionalStoredCert = certIssuer.listCerts().stream()
                .filter(cert -> cert.getCertificate().getSerial()
                .equals(issuerId))
                .findAny();
        if(optionalStoredCert.isEmpty()){
            //TODO: Certificate not found
        }

        //TODO: Implement
        throw new UnsupportedOperationException();

    }

    private Optional<Response> checkIssuerId(String issuerId, boolean mustExist){
        Set<Violation> violationSet = new HashSet<>();
        if(!ID_PATTERN.matcher(issuerId).matches())
            violationSet.add(new Violation("path.issuerId", ViolationType.FORMAT,
                    "issuerId does not match regular expression: "+ ID_REGEX));
        if(mustExist && !registry.exists(issuerId)){
            LOGGER.warn("Issuer not found: " +  issuerId);
            return Optional.of(Response.status(409).entity(
                            new Problem("/problems/issuer/not-found",
                                    "Issuer not found", 404,
                                    "There no issuer with a matching ID: " + issuerId))
                    .build());
        }
        if(!mustExist && registry.exists(issuerId)){
            LOGGER.warn("Issuer id taken: " +  issuerId);
            return Optional.of(Response.status(409).entity(
                            new Problem("/problems/issuerId/id-taken",
                                    "Issuer ID Taken", 409,
                                    "There is already an issuerId with the provided ID: " + issuerId))
                    .build());
        }
        if(!violationSet.isEmpty()) {
            return Optional.of(Response.status(422).entity(
                    new ConstraintViolationProblem(violationSet))
                    .build());
        }
        return Optional.empty();
    }

}
