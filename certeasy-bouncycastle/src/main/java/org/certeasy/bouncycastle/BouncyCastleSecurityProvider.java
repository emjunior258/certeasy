package org.certeasy.bouncycastle;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public final class BouncyCastleSecurityProvider {

    static void install(){
        Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if(provider == null)
            Security.addProvider(new BouncyCastleProvider());
    }

}
