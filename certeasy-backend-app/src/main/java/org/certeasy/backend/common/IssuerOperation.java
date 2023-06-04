package org.certeasy.backend.common;


import org.certeasy.backend.issuer.CertIssuer;

import javax.ws.rs.core.Response;

@FunctionalInterface
public interface IssuerOperation {

    Response getResponse(CertIssuer issuer);

}
