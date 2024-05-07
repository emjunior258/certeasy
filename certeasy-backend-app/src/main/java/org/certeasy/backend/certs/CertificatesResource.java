package org.certeasy.backend.certs;

import org.certeasy.backend.Result;
import org.certeasy.backend.common.BaseResource;
import org.certeasy.backend.common.problem.ProblemResponse;
import org.certeasy.backend.common.validation.Violation;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Path("/api/certificates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CertificatesResource extends BaseResource {

    private static final Logger LOGGER = Logger.getLogger(CertificatesResource.class);

    @GET
    public Response listCerts(@QueryParam("type") String type){
        IssuedCertType certType;
        if(type != null && !type.isBlank()){
            Result<IssuedCertType,Violation> typeCheckResult = checkCertType(type);
            Optional<Violation> optionalViolation = typeCheckResult.getError();
            Optional<IssuedCertType> optionalIssuedCertType = typeCheckResult.getOkValue();
            if(optionalViolation.isPresent() && optionalIssuedCertType.isEmpty()) {
                return ProblemResponse.badQueryParameter(
                        optionalViolation.get());
            }else certType = optionalIssuedCertType.orElse(null);
        } else certType = null;

        Stream<CertificateSummaryInfo> stream = registry()
                .list()
                .stream()
                .flatMap(certIssuer -> certIssuer.listCerts().stream()
                        .map(storedCert -> CertificateConverter.toSummaryInfo(
                                storedCert.getCertificate(),
                                certIssuer)
                    )
                );
        if(certType != null) {
            LOGGER.debug("Listing certificates filtered by type " +  certType);
            stream = stream.filter(cert -> cert.getType() == certType);
        }else LOGGER.debugf("Listing certificates without filter");

        return Response.status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity(stream.collect(Collectors.toSet()))
                .build();

    }

}
