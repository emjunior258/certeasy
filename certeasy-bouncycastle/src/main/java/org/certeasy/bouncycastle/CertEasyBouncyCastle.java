package org.certeasy.bouncycastle;

import org.certeasy.CertEasyContext;
import org.certeasy.CertificateGenerator;
import org.certeasy.PEMCoder;

public class CertEasyBouncyCastle implements CertEasyContext  {

    private PEMCoder pemCoder;
    private CertificateGenerator generator;

    public CertEasyBouncyCastle(){
        this.pemCoder = new BouncyCastlePEMCoder();
        this.generator = new BouncyCastleCertGenerator();
    }

    @Override
    public CertificateGenerator generator() {
        return generator;
    }

    @Override
    public PEMCoder pemCoder() {
        return pemCoder;
    }
}
