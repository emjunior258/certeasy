package org.certeasy.backend;

import io.quarkus.runtime.StartupEvent;
import org.certeasy.CertEasyContext;
import org.certeasy.CertEasyException;
import org.certeasy.backend.persistence.IssuerDatastore;
import org.certeasy.backend.persistence.directory.DirectoryIssuerDatastore;
import org.certeasy.bouncycastle.CertEasyBouncyCastle;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@ApplicationScoped
public class DirectoryInitializer {

    private IssuerDatastore store;

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DirectoryInitializer.class);

    private CertEasyContext context = new CertEasyBouncyCastle();

    public void initialize(@Observes StartupEvent event, @ConfigProperty(name = CertConstants.DATA_DIRECTORY_CONFIG) String dataDirectory ){
        File dataDir = new File(dataDirectory);
        if(!dataDir.exists()) {
            throw new CertEasyException("Data directory not found: "+dataDir.getAbsolutePath());
        }
        File certificatesDir = new File(dataDir,CertConstants.CERTIFICATES_SUBDIR);
        if(!certificatesDir.exists()){
            LOGGER.info("Creating certificates subdirectory");
            try {
                Files.createDirectories(certificatesDir.toPath());
                LOGGER.info("Certificates subdirectory created successfully");
            }catch (IOException ex){
                throw new CertEasyException("failed to create certificates subdirectory", ex);
            }
        }else LOGGER.info("Certificates subdirectory already exists");
        this.store = new DirectoryIssuerDatastore(dataDir, context);
    }

    @Produces @ApplicationScoped
    public IssuerDatastore getStore(){
        return this.store;
    }

    @Produces @ApplicationScoped
    public CertEasyContext getContext(){
        return this.context;
    }

}
