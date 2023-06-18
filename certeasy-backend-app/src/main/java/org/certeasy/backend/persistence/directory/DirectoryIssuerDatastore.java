package org.certeasy.backend.persistence.directory;

import org.apache.commons.io.FileUtils;
import org.certeasy.CertEasyContext;
import org.certeasy.CertEasyException;
import org.certeasy.Certificate;
import org.certeasy.PEMCoderException;
import org.certeasy.backend.CertConstants;
import org.certeasy.backend.persistence.IssuerDatastore;
import org.certeasy.backend.persistence.IssuerDatastoreException;
import org.certeasy.backend.persistence.StoredCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class DirectoryIssuerDatastore implements IssuerDatastore {

    private File directory;

    private File serialFile;


    private Map<String, StoredCert> cache = new HashMap<>();

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DirectoryIssuerDatastore.class);

    private boolean directoryScanned = false;

    private CertEasyContext context;

    public DirectoryIssuerDatastore(File dir, CertEasyContext context){
        if(dir==null || !dir.exists() || !dir.isDirectory())
            throw new IllegalArgumentException("dir MUST not be null and MUST point to an existing directory");
        if(context==null)
            throw new IllegalArgumentException("context MUST not be null");
        this.directory = dir;
        this.context = context;
        this.serialFile = new File(dir, CertConstants.ISSUER_CERT_SERIAL_FILENAME);
    }

    @Override
    public void put(Certificate certificate){
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        String serial = certificate.getSerial();
        LOGGER.info("Storing certificate with serial {}", serial);
        File certDirectory = new File(directory, serial);
        DirectoryStoredCert storedCert = new DirectoryStoredCert(certDirectory, certificate, context);
        LOGGER.debug("Writing certificate {} files", serial);
        storedCert.writeFiles();
        LOGGER.debug("Adding certificate {} to cache", serial);
        this.cache.put(certificate.getSerial(),storedCert);
        LOGGER.info("Certificate {} stored successfully", serial);
    }

    @Override
    public Collection<StoredCert> listStored(){
        LOGGER.info("Listing stored certificates");
        this.scanIfNotYet();
        return cache.values();
    }

    @Override
    public Optional<StoredCert> getCert(String serial){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        LOGGER.info("Getting certificate with serial {}", serial);
        this.scanIfNotYet();
        StoredCert storedCert = cache.get(serial);
        if(storedCert!=null)
            LOGGER.warn("Certificate {} not found in cache", serial);
        else LOGGER.info("Certificate {} found in cache", serial);
        return Optional.ofNullable(storedCert);
    }


    @Override
    public void deleteCert(String serial){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        LOGGER.info("Delete certificate with serial {}", serial);
        DirectoryStoredCert storedCert = (DirectoryStoredCert) cache.get(serial);
        if(storedCert!=null){
            LOGGER.debug("Certificate {} found in cache", serial);
            LOGGER.debug("Deleting certificate {} files", serial);
            storedCert.deleteFiles();
            LOGGER.debug("Removing certificate {} from cache", serial);
            cache.remove(serial);
            LOGGER.info("Certificate {} deleted successfully", serial);
        }else LOGGER.warn("Certificate {} not found in cache", serial);
    }

    @Override
    public Optional<String> getIssuerCertSerial() throws IssuerDatastoreException {
        if(serialFile.exists()){
            try {
                return Optional.of(FileUtils.readFileToString(serialFile,
                        StandardCharsets.UTF_8));
            } catch (IOException ex) {
                throw new CertEasyException("failed to read issuerId certificate serial file",
                        ex);
            }
        }else LOGGER.warn("Serial file not found. Nothing to Read");
        return Optional.empty();
    }

    @Override
    public void putIssuerCertSerial(String serial) throws IssuerDatastoreException {
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(!serialFile.exists()){
            try {
                FileUtils.writeStringToFile(serialFile, serial, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                throw new CertEasyException("failed to write issuerId certificate serial to file",
                        ex);
            }
        }
    }

    @Override
    public void purge() throws IssuerDatastoreException {
        try {
            if(!directory.exists())
                return;
            LOGGER.info("Deleting issuerId data directory: {}",directory.getAbsolutePath());
            FileUtils.deleteDirectory(directory);
        } catch (IOException ex) {
            throw new IssuerDatastoreException("error deleting data directory: "+directory.getAbsolutePath(),
                    ex);
        }
    }

    private void scanDirectory(){
        LOGGER.info("Starting directory scan");
        LOGGER.info("Listing all files in {}", directory.getAbsolutePath());
        File[] files = directory.listFiles(it -> it.isDirectory() && !it.getName().equals(".") && !it.getName().equals(".."));
        if(files==null || files.length==0){
            LOGGER.warn("Scan finished: No sub-directories found");
            return;
        }
        Arrays.stream(files).forEach(file -> {
            LOGGER.info("Found certificate directory {}", file.getName());
            try {
                StoredCert storedCert = new DirectoryStoredCert(file, context);
                Certificate certificate = storedCert.getCertificate();
                LOGGER.info("Loaded certificate with serial {}", certificate.getSerial());
                cache.put(certificate.getSerial(), storedCert);
            } catch (IllegalArgumentException | IssuerDatastoreException | PEMCoderException ex) {
                LOGGER.warn("Failed to Load certificate from directory {}", file.getName(), ex);
            }
        });
        LOGGER.info("Directory scan complete");
    }

    private void scanIfNotYet(){
        if(!directoryScanned)
            this.scanDirectory();
    }

}
