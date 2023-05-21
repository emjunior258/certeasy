package org.certeasy.backend.common.cert;


import org.certeasy.backend.common.problem.Problem;

public class CertificateNotFound extends Problem {

    public CertificateNotFound(String detail) {
        super("/problems/not-found", "Certificate Not Found", 404, detail);
    }

}
