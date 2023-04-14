package org.certeasy;

import java.util.Optional;

public record CertEasyContext(CertificateGenerator generator, PEMCoder pemCoder) {

    private static final ThreadLocal<CertEasyContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public CertEasyContext(CertificateGenerator generator, PEMCoder pemCoder){
        if(generator ==null)
            throw new IllegalArgumentException("generator MUST not be null");
        if(pemCoder ==null)
            throw new IllegalArgumentException("pemCoder MUST not be null");
        this.generator = generator;
        this.pemCoder = pemCoder;
    }


    public static void activate(CertEasyContext context){
        if(context==null)
            throw new IllegalArgumentException("context MUST not be null");
        CONTEXT_THREAD_LOCAL.set(context);
    }

    public static void clear(){
        CONTEXT_THREAD_LOCAL.remove();
    }

    public static Optional<CertEasyContext> current(){
        return Optional.ofNullable(CONTEXT_THREAD_LOCAL.get());
    }


    public static CertEasyContext get() {
        Optional<CertEasyContext> optionalCertEasyContext = current();
        if(optionalCertEasyContext.isEmpty())
            throw new IllegalStateException("there is no current context set");
        return optionalCertEasyContext.get();
    }


}
