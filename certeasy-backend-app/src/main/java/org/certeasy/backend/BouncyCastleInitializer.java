package org.certeasy.backend;

import io.quarkus.runtime.StartupEvent;
import org.certeasy.bouncycastle.BouncyCastleSecurityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
@ApplicationScoped
public class BouncyCastleInitializer {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(BouncyCastleInitializer.class);

    public void initialize(@Observes StartupEvent event ){
        LOGGER.info("Installing BouncyCastle security provider");
        BouncyCastleSecurityProvider.install();
    }

}
