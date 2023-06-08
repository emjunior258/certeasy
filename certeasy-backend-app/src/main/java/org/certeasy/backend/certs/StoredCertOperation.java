package org.certeasy.backend.certs;


import org.certeasy.backend.persistence.StoredCert;

import javax.ws.rs.core.Response;

@FunctionalInterface
public interface StoredCertOperation {

    Response getResponse(StoredCert issuedCert);

}
