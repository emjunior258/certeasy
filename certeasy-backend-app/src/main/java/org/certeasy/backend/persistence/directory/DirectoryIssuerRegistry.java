package org.certeasy.backend.persistence.directory;

import org.certeasy.CertEasyContext;
import org.certeasy.Certificate;
import org.certeasy.backend.CertConstants;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.persistence.IssuerDatastore;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.certeasy.backend.persistence.IssuerRegistryException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


@ApplicationScoped
public class DirectoryIssuerRegistry implements IssuerRegistry {

    private final File dataDirectory;
    private final CertEasyContext context;
    private final Map<String, CertIssuer> cache = new HashMap<>();
    private boolean scanned = false;
    private static final Logger LOGGER = Logger.getLogger(DirectoryIssuerRegistry.class);

    public DirectoryIssuerRegistry(@ConfigProperty(name = CertConstants.DATA_DIRECTORY_CONFIG) String dataDirectory, CertEasyContext context){
        if(dataDirectory==null || dataDirectory.isEmpty())
            throw new IllegalArgumentException("dataDirectory path cannot be null or empty");
        if(context==null)
            throw new IllegalArgumentException("context must not be null");
        this.dataDirectory = new File(dataDirectory);
        if(!this.dataDirectory.exists() || !this.dataDirectory.isDirectory())
            throw new IllegalArgumentException("dataDirectory path must point to existing directory");
        this.context = context;
    }

    @Override
    public Collection<CertIssuer> list() throws IssuerRegistryException {
        this.scanIfNotYet();
        return cache.values();
    }

    private void scanIfNotYet() throws IssuerRegistryException {
        if(!scanned)
            this.scanDirectory();
    }

    private void scanDirectory(){
        LOGGER.info("Scanning data directory...");
        File[] files = this.dataDirectory.listFiles(it -> it.isDirectory() && !it.getName().equals(".") &&
                        !it.getName().equals(".."));
        if(files==null)
            return;
        for(File issuerDirectory : files){
            IssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory,context);
            CertIssuer issuer = new CertIssuer(issuerDirectory.getName(),datastore,context);
            if(!issuer.hasCertificate()){
                LOGGER.warn(String.format("Skipping directory: %s", issuerDirectory.getAbsolutePath()));
                continue;
            }
            LOGGER.info("Found issuerId: " + issuerDirectory.getName());
            cache.put(issuerDirectory.getName(), issuer);
        }
        this.scanned = true;
    }

    @Override
    public CertIssuer add(String issuerId, Certificate certificate) throws IssuerRegistryException {
        if(issuerId ==null || issuerId.isEmpty())
            throw new IllegalArgumentException("name MUST not be null nor empty");
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        LOGGER.info("Adding issuerId: " + issuerId);
        File issuerDirectory = new File(dataDirectory, issuerId);
        LOGGER.info("Creating issuerId data directory: " + issuerDirectory.getAbsolutePath());
        try {
            Files.createDirectories(issuerDirectory.toPath());
        } catch (IOException ex) {
            throw new IssuerRegistryException("error creating issuerId data directory: "+issuerDirectory.getAbsolutePath(),
                    ex);
        }
        IssuerDatastore datastore = new DirectoryIssuerDatastore(issuerDirectory, context);
        CertIssuer issuer = new CertIssuer(issuerId, datastore, context, certificate);
        cache.put(issuerId, issuer);
        LOGGER.info("Issuer added successfully: " + issuerId);
        return issuer;
    }

    @Override
    public boolean exists(String issuerId) throws IssuerRegistryException {
        if(issuerId ==null || issuerId.isEmpty())
            throw new IllegalArgumentException("name MUST not be null");
        this.scanIfNotYet();
        return(cache.containsKey(issuerId));
    }

    @Override
    public Optional<CertIssuer> getById(String issuerId) throws IssuerRegistryException {
        if(issuerId ==null || issuerId.isEmpty())
            throw new IllegalArgumentException("name MUST not be null");
        this.scanIfNotYet();
        return Optional.ofNullable(cache.get(issuerId));
    }

    @Override
    public void delete(CertIssuer issuer) throws IssuerRegistryException {
        if(issuer==null)
            throw new IllegalArgumentException("issuerId MUST not be null");
        if(!issuer.isDisabled())
            issuer.disable();
        this.cache.remove(issuer.getId());
    }

}
