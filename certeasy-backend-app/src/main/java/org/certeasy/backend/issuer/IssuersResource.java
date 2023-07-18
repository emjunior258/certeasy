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
import org.certeasy.backend.persistence.StoredCert;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;
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
    @Path("/{issuerId}/cert-spec")
    public Response createFromSpec(@PathParam("issuerId") String issuerId, SubCaSpec spec){
        return this.checkIssuerNotExistsThen(issuerId, () -> {
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
            registry().add(issuerId, certificate);
            return Response.noContent().build();
        });
    }

    @POST
    @Path("/{issuerId}/cert-pem")
    public Response createFromPem(@PathParam("issuerId") String issuerId, CertPEM pem){
        return this.checkIssuerNotExistsThen(issuerId,  ()->{
            Set<Violation> violationSet = pem.validate(ValidationPath.of("body"));
            if(!violationSet.isEmpty())
                return ProblemResponse.constraintViolations(violationSet);
            try{
                Certificate certificate = context().pemCoder().decodeCertificate(pem.certFile(),
                        pem.keyFile());
                BasicConstraints basicConstraints = certificate.getBasicConstraints();
                if(basicConstraints==null || !basicConstraints.ca())
                    return Response.status(422).entity(new ConstraintViolationProblem(
                            new Violation("body.pem.cert_file", ViolationType.STATE,
                                    "not_ca", "cert_file is does not have CA basic constraint"
                            ))).build();
                registry().add(issuerId, certificate);
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
        });
    }

    @POST
    @Path("/{issuerId}/cert-ref")
    public Response createFromRef(@PathParam("issuerId") String issuerId, IssuerCertRef ref) {
        return this.checkIssuerNotExistsThen(issuerId, () -> {
            Set<Violation> violationSet = ref.validate(ValidationPath.of("body"));
            if (!violationSet.isEmpty())
                return ProblemResponse.constraintViolations(violationSet);
            Optional<CertIssuer> optionalCertIssuer = registry().getById(ref.issuerId());
            //Issuer not found
            if (optionalCertIssuer.isEmpty()) {
                return ProblemResponse.constraintViolation(
                        new Violation("body.issuer_id", ViolationType.STATE,
                                "not-found",
                                "No issuer found with a matching Id: " + ref.issuerId()
                        ));
            }
            CertIssuer certIssuer = optionalCertIssuer.get();
            Optional<StoredCert> optionalStoredCert = certIssuer.listCerts().stream()
                    .filter(cert -> cert.getCertificate().getSerial().equals(ref.serial()))
                    .findAny();
            //Certificate not found
            if (optionalStoredCert.isEmpty()) {
                return ProblemResponse.constraintViolation(
                        new Violation("body.serial", ViolationType.STATE,
                                "not-found",
                                "No certificate found with a matching serial: " + ref.serial()
                        ));
            }
            Certificate certificate = optionalStoredCert.get().getCertificate();
            //Certificate not a CA
            if (!certificate.getBasicConstraints().ca()) {
                return ProblemResponse.constraintViolation(
                        new Violation("body.serial", ViolationType.STATE,
                                "not-ca",
                                "The referenced certificate is not a CA: " + ref.serial()
                        ));
            }
            registry().add(issuerId, certificate);
            return Response.noContent().build();
        });
    }

}
