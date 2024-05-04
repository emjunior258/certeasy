package org.certeasy.backend.certs;

import org.certeasy.backend.common.BaseResource;
import org.certeasy.backend.issuer.CertIssuer;
import org.jboss.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Path("/api/certificates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CertificatesResource extends BaseResource {

    private static final Logger LOGGER = Logger.getLogger(CertificatesResource.class);

    @GET
    public Response listCerts(){
        Set<CertificateSummaryInfo> summaryInfoSet = registry()
                .list()
                .stream()
                .flatMap(certIssuer -> certIssuer.listCerts().stream()
                        .map(storedCert -> CertificateConverter.toSummaryInfo(
                                storedCert.getCertificate(),
                                certIssuer)
                    )
                ).collect(Collectors.toSet());

        return Response.status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity(summaryInfoSet)
                .build();

    }

}
