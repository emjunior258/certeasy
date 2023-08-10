package org.certeasy.backend.persistence.directory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.certeasy.CertEasyContext;
import org.certeasy.Certificate;
import org.certeasy.PEMCoder;
import org.certeasy.backend.persistence.AbstractStoredCert;
import org.certeasy.backend.persistence.IssuerDatastoreException;
import org.certeasy.backend.persistence.StoredCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class DirectoryStoredCert extends AbstractStoredCert implements StoredCert {
    private Certificate certificate;
    private File directory;
    private File certFile;
    private File keyFile;

    private String certPem;
    private String keyPem;

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DirectoryStoredCert.class);

    private CertEasyContext context;

    DirectoryStoredCert(File directory, Certificate certificate, CertEasyContext context){
        this.directory = directory;
        this.certificate = certificate;
        this.context = context;
        PEMCoder pemCoder = context.pemCoder();
        this.certPem = pemCoder.encodeCert(certificate);
        this.keyPem = pemCoder.encodePrivateKey(certificate);
        this.initFiles();
    }

    DirectoryStoredCert(File directory, CertEasyContext context){
        this.directory = directory;
        this.context = context;
        this.initFiles();
        if(!certFile.exists())
            throw new IllegalArgumentException("cert file missing: "+certFile.getAbsolutePath());
        if(!keyFile.exists())
            throw new IllegalArgumentException("key file missing: "+certFile.getAbsolutePath());

    }

    private void initFiles(){
        this.certFile = new File(directory,"cert.pem");
        this.keyFile = new File(directory,"key.pem");
    }

    @Override
    public Certificate getCertificate(){
        if(certificate==null)
            loadCert();
        return certificate;
    }

    @Override
    public String getKeyPem() throws IssuerDatastoreException {
        if(keyPem==null)
            loadCert();
        return this.keyPem;
    }

    @Override
    public String getCertPem() throws IssuerDatastoreException {
        if(certPem==null)
            loadCert();
        return this.keyPem;
    }

    private void loadCert(){
        LOGGER.debug("Cert PEM file is stored at {}", certFile.getAbsolutePath());
        LOGGER.debug("Key PEM file is stored at {}", keyFile.getAbsolutePath());
        LOGGER.info("Reading Cert PEM into memory");
        try {
            certPem = IOUtils.toString(new FileInputStream(certFile), StandardCharsets.UTF_8);
        }catch (IOException ex){
            throw new IssuerDatastoreException("error reading cert PEM file: "+certFile.getAbsolutePath(),
                    ex);
        }
        LOGGER.info("Reading Key PEM into memory");
        try {
            keyPem = IOUtils.toString(new FileInputStream(keyFile), StandardCharsets.UTF_8);
        }catch (IOException ex){
            throw new IssuerDatastoreException("error reading key PEM file: "+keyFile.getAbsolutePath(),
                    ex);
        }
        LOGGER.info("Decoding certificate from memory data");
        this.certificate = context.pemCoder().decodeCertificate(certPem, keyPem);
        LOGGER.info("Certificate decoded successfully");
    }


    void writeFiles(){
        this.ensureDirectoryExists();
        try {
            FileUtils.writeStringToFile(certFile, certPem, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IssuerDatastoreException("error writing cert PEM file: "+certFile.getAbsolutePath(),
                    ex);
        }
        try {
            FileUtils.writeStringToFile(keyFile, keyPem, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IssuerDatastoreException("error writing key PEM file: "+keyFile.getAbsolutePath(),
                    ex);
        }
    }

    private void ensureDirectoryExists(){
        if(!directory.exists()) {
            LOGGER.info("Creating Certificate directory {}", directory.getAbsolutePath());
            if(directory.mkdirs()){
                LOGGER.info("Directory created successfully {}", directory.getAbsolutePath());
            }else throw new IssuerDatastoreException("failed to create directory: "+directory.
                    getAbsolutePath());
        }
    }

    void deleteFiles(){
        try {
            if(certFile.exists()) {
                LOGGER.info("Deleting cert file: "+certFile.getAbsolutePath());
                Files.delete(certFile.toPath());
            }
        } catch (IOException ex) {
            throw new IssuerDatastoreException("error deleting cert file: "+certFile.getAbsolutePath(),
                    ex);
        }
        try {
            if(keyFile.exists()) {
                LOGGER.info("Deleting key file: "+certFile.getAbsolutePath());
                Files.delete(keyFile.toPath());
            }
        } catch (IOException ex) {
            throw new IssuerDatastoreException("error deleting key file: "+keyFile.getAbsolutePath(),
                    ex);
        }
        try {
            if(directory.exists()) {
                LOGGER.info("Deleting cert directory: "+directory.getAbsolutePath());
                Files.delete(directory.toPath());
            }
        } catch (IOException ex) {
            throw new IssuerDatastoreException("error deleting certificate directory: "+directory.getAbsolutePath(),
                    ex);
        }
    }

    public File getDirectory() {
        return directory;
    }
}
