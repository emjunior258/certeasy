package org.certeasy.backend.issuer.rest;

import org.certeasy.*;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.problem.Problem;
import org.certeasy.backend.common.problem.ServerErrorProblem;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.certeasy.backend.issuer.IssuerCertInfo;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.certspec.CertificateAuthoritySpec;
import org.certeasy.certspec.CertificateAuthoritySubject;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Path("/api/issuers/{id}")
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

    @POST
    public Response createIssuer(@PathParam("id") String id, NewIssuerCert cert){
        Set<Violation> violationSet = new HashSet<>();
        if(!ID_PATTERN.matcher(id).matches())
            violationSet.add(new Violation("path.id", ViolationType.FORMAT,
                    "id does not match regular expression: "+ ID_REGEX));

        if(registry.exists(id)){
            LOGGER.warn("Issuer id taken: " +  id);
            return Response.status(409).entity(
                    new Problem("/problems/issuer/id-taken",
                            "Issuer ID Taken", 409,
                            "There is already an issuer with the provided ID: " + id))
                    .build();
        }

        violationSet.addAll(cert.validate(ValidationPath.of("body")));
        if(!violationSet.isEmpty()) {
            return Response.status(422).entity(new ConstraintViolationProblem(violationSet))
                    .build();
        }
        if(cert.spec() != null){
            return this.createFromSpec(id, cert.spec());
        }else if(cert.pem() != null){
            return this.createFromPem(id, cert.pem());
        }
        //TODO: Implement
        throw new UnsupportedOperationException();
    }

    private Response createFromSpec(String name, IssuerCertInfo info){
        CertificateAuthoritySubject subject = new CertificateAuthoritySubject(
                info.name(),
                info.geographicAddressInfo().toGeographicAddress());
        CertificateAuthoritySpec spec = new CertificateAuthoritySpec(subject, info.pathLength(),
                KeyStrength.valueOf(info.keyStrength()),
                info.validity().toDateRange());
        Certificate certificate = context.generator().generate(spec);
        registry.add(name, certificate);
        return Response.noContent().build();
    }

    private Response createFromPem(String name, IssuerCertPEM pem){
        try {
            Certificate certificate = context.pemCoder().decodeCertificate(pem.certFile(),
                    pem.keyFile());
            BasicConstraints basicConstraints = certificate.getBasicConstraints();
            if(basicConstraints==null || !basicConstraints.ca())
                return Response.status(422).entity(new ConstraintViolationProblem(
                        new Violation("body.pem.cert_file", ViolationType.STATE,
                                "not_ca", "cert_file is does not have CA basic constraint"
                        ))).build();
            registry.add(name, certificate);
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

}
