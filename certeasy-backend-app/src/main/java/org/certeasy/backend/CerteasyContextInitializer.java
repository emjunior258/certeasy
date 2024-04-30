package org.certeasy.backend;

import org.certeasy.CertEasyContext;
import org.certeasy.bouncycastle.CertEasyBouncyCastle;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;


@ApplicationScoped
public class CerteasyContextInitializer {

    private CertEasyContext context = new CertEasyBouncyCastle();

    @Produces @ApplicationScoped
    public CertEasyContext getContext(){
        return this.context;
    }

}
