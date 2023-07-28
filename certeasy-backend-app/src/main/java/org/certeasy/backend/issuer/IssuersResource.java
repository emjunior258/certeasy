package org.certeasy.backend.issuer;

import org.certeasy.*;
import org.certeasy.backend.common.BaseResource;
import org.certeasy.backend.common.CertPEM;
import org.certeasy.backend.common.SubCaSpec;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.problem.ProblemResponse;
import org.certeasy.backend.common.problem.ServerErrorProblem;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.certeasy.backend.persistence.IssuerDuplicationException;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/api/issuers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IssuersResource extends BaseResource {

    private static final Logger LOGGER = Logger.getLogger(IssuersResource.class);

    @GET
    public Response listIssuers(){
        Collection<CertIssuer> issuers =this.registry().list();
        Set<IssuerInfo> issuerInfoSet = issuers.stream().filter(CertIssuer::hasCertificate).map(item -> {
            Certificate certificate = item.getCertificate().get();
            String id = item.getId();
            String serial = certificate.getSerial();
            IssuerType issuerType = certificate.isSelfSignedCA() ? IssuerType.ROOT : IssuerType.SUB_CA;
            String distinguishedName = certificate.getDistinguishedName().toString();
            return new IssuerInfo(id, serial, issuerType,
                    distinguishedName, certificate.getBasicConstraints()
                    .pathLength());
        }).collect(Collectors.toSet());
        return Response.ok()
                .entity(issuerInfoSet).build();
    }

    @DELETE
    @Path("/{issuerId}")
    public Response deleteIssuer(@PathParam("issuerId") String issuerId){
        return this.checkIssuerExistsThen(issuerId, (issuer) -> {
            registry().delete(issuer);
            return Response.noContent().build();
        });
    }

    @POST
    @Path("/cert-spec")
    public Response createFromSpec(SubCaSpec spec){
        Set<Violation> violationSet = spec.validate(ValidationPath.of("body"));
        if(!violationSet.isEmpty())
            return ProblemResponse.constraintViolations(violationSet);
        CertificateAuthoritySubject subject = new CertificateAuthoritySubject(
                spec.getName(),
                spec.getGeographicAddressInfo().
                        toGeographicAddress());
        CertificateAuthoritySpec authoritySpec = new CertificateAuthoritySpec(subject, spec.getPathLength(),
                KeyStrength.valueOf(spec.getKeyStrength()),
                spec.getValidity().toDateRange());
        Certificate certificate = context().generator().generate(authoritySpec);
        try {
            CertIssuer certIssuer = registry().add(certificate);
            return Response.ok(new CreatedIssuerInfo(certIssuer.getId())).build();
        }catch (IssuerDuplicationException ex){
            return Response.status(409).entity(ex.asProblem()).build();
        }
    }

    @POST
    @Path("/cert-pem")
    public Response createFromPem(CertPEM pem){
        Set<Violation> violationSet = pem.validate(ValidationPath.of("body"));
        if(!violationSet.isEmpty()) {
            LOGGER.debug(String.format("%d constraint violations found", violationSet.size()));
            return ProblemResponse.constraintViolations(violationSet);
        }
        try{
            Certificate certificate = context().pemCoder().decodeCertificate(pem.certFile(),
                    pem.keyFile());
            BasicConstraints basicConstraints = certificate.getBasicConstraints();
            if(basicConstraints==null || !basicConstraints.ca()) {
                LOGGER.debug("Basic constraints extension missing on certificate");
                return Response.status(422).entity(new ConstraintViolationProblem(
                        new Violation("body.pem.cert_file", ViolationType.STATE,
                                "not_ca", "cert_file is does not have CA basic constraint"
                        ))).build();
            }
            CertIssuer issuer = registry().add(certificate);
            return Response.ok(new CreatedIssuerInfo(issuer.getId())).build();
        }catch (IllegalCertPemException ex) {
            LOGGER.debug("Certificate not a valid PEM", ex);
            return Response.status(422).entity(new ConstraintViolationProblem(
                    new Violation("body.pem.cert_file", ViolationType.FORMAT,
                            "cert_file is NOT a valid PEM encoded certificate"
                    ))).build();
        }catch (IllegalPrivateKeyPemException ex){
            LOGGER.debug("Key not a valid PEM", ex);
            return Response.status(422).entity(new ConstraintViolationProblem(
                    new Violation("body.pem.key_file", ViolationType.FORMAT,
                            "key_file is NOT a valid PEM encoded private key"
                    ))).build();
        }catch (PEMCoderException ex){
            String message = "Error decoding pem content";
            LOGGER.error(message, ex);
            return Response.status(500).entity(new ServerErrorProblem(message))
                    .build();
        }catch (IssuerDuplicationException ex){
            return Response.status(409).entity(ex.asProblem()).build();
        }
    }

}
