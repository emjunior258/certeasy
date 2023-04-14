package org.certeasy.persistence.fs;

import org.certeasy.Certificate;
import org.certeasy.persistence.CertificateStore;
import org.certeasy.persistence.CertificateStoreException;
import org.certeasy.persistence.StoredCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class DirectoryCertificateStore implements CertificateStore {

    private File directory;
    private Map<String, StoredCert> cache = new HashMap<>();

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DirectoryCertificateStore.class);

    private boolean directoryScanned = false;

    public DirectoryCertificateStore(File dir){
        if(dir==null || !dir.exists() || !dir.isDirectory())
            throw new IllegalArgumentException("dir MUST not be null and MUST point to an existing directory");
        this.directory = dir;
    }

    @Override
    public void store(Certificate certificate){
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        String serial = certificate.getSerial();
        LOGGER.info("Storing certificate with serial {}", serial);
        File certDirectory = new File(directory, serial);
        DirectoryStoredCert storedCert = new DirectoryStoredCert(certDirectory, certificate);
        LOGGER.debug("Writing certificate {} files", serial);
        storedCert.writeFiles();
        LOGGER.debug("Adding certificate {} to cache", serial);
        this.cache.put(certificate.getSerial(),storedCert);
        LOGGER.info("Certificate {} stored successfully", serial);
    }

    @Override
    public Collection<StoredCert> listStored(){
        LOGGER.info("Listing stored certificates");
        if(!directoryScanned)
            this.scanDirectory();
        return cache.values();
    }

    @Override
    public Optional<StoredCert> getCert(String serial){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        LOGGER.info("Getting certificate with serial {}", serial);
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
                StoredCert storedCert = new DirectoryStoredCert(file);
                Certificate certificate = storedCert.getCertificate();
                LOGGER.info("Loaded certificate with serial {}", certificate.getSerial());
                cache.put(certificate.getSerial(), storedCert);
            } catch (IllegalArgumentException | CertificateStoreException ex) {
                LOGGER.warn("Failed to Load certificate from directory {}", file.getName(), ex);
            }
        });
        LOGGER.info("Directory scan complete");
    }

}
