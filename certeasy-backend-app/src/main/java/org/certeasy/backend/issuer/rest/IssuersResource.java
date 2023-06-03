package org.certeasy.backend.issuer.rest;


import org.certeasy.Certificate;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.persistence.IssuerRegistry;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/api/issuers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IssuersResource {

    @Inject
    IssuerRegistry registry;

    @GET
    public Response listIssuers(){
        Collection<CertIssuer> issuers =this.registry.list();
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

    /*
    @GET
    public Response getCertInfo(){
        Optional<Certificate> optionalCertificate = certManager.getCertificate();
        if(optionalCertificate.isEmpty()){
            return Response.status(404)
                    .entity(new CertificateNotFound("There is no issuerId certificate set"))
                    .build();
        }
        Certificate certificate = optionalCertificate.get();
        return Response.ok(CertificateConverter.convert(certificate))
                .build();
    }*/



}
