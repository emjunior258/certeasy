package org.certeasy.persistence.fs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.certeasy.CertEasyContext;
import org.certeasy.Certificate;
import org.certeasy.PEMCoder;
import org.certeasy.persistence.CertificateStoreException;
import org.certeasy.persistence.StoredCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class DirectoryStoredCert implements StoredCert {
    private Certificate certificate;
    private File directory;
    private File certFile;
    private File keyFile;

    private String certPem;
    private String keyPem;

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DirectoryStoredCert.class);

    DirectoryStoredCert(File directory, Certificate certificate){
        this.directory = directory;
        this.certificate = certificate;
        PEMCoder pemCoder = CertEasyContext.get().pemCoder();
        this.certPem = pemCoder.encodeCert(certificate);
        this.keyPem = pemCoder.encodePrivateKey(certificate);
    }

    DirectoryStoredCert(File directory){
        this.directory = directory;
        this.certFile = new File(directory,"cert.pem");
        this.keyFile = new File(directory,"key.pem");
        if(!certFile.exists())
            throw new IllegalArgumentException("cert file missing: "+certFile.getAbsolutePath());
        if(!keyFile.exists())
            throw new IllegalArgumentException("key file missing: "+certFile.getAbsolutePath());

    }

    @Override
    public Certificate getCertificate(){
        if(certificate==null)
            loadCert();
        return certificate;
    }

    @Override
    public String getKeyPem() throws CertificateStoreException {
        if(keyPem==null)
            loadCert();
        return this.keyPem;
    }

    @Override
    public String getCertPem() throws CertificateStoreException {
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
            throw new CertificateStoreException("error reading cert PEM file: "+certFile.getAbsolutePath(),
                    ex);
        }
        LOGGER.info("Reading Key PEM into memory");
        try {
            keyPem = IOUtils.toString(new FileInputStream(keyFile), StandardCharsets.UTF_8);
        }catch (IOException ex){
            throw new CertificateStoreException("error reading key PEM file: "+keyFile.getAbsolutePath(),
                    ex);
        }
        LOGGER.info("Decoding certificate from memory data");
        CertEasyContext.get().pemCoder().decodeCertificate(certPem, keyPem);
        LOGGER.info("Certificate decoded successfully");
    }


    void writeFiles(){
        this.ensureDirectoryExists();
        try {
            FileUtils.writeStringToFile(certFile, certPem, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new CertificateStoreException("error writing cert PEM file: "+certFile.getAbsolutePath(),
                    ex);
        }
        try {
            FileUtils.writeStringToFile(keyFile, keyPem, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new CertificateStoreException("error writing key PEM file: "+keyFile.getAbsolutePath(),
                    ex);
        }
    }

    private void ensureDirectoryExists(){
        if(!directory.exists()) {
            LOGGER.info("Creating Certificate directory {}", directory.getAbsolutePath());
            if(directory.mkdirs()){
                LOGGER.info("Directory created successfully {}", directory.getAbsolutePath());
            }else throw new CertificateStoreException("failed to create directory: "+directory.
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
            throw new CertificateStoreException("error deleting cert file: "+certFile.getAbsolutePath(),
                    ex);
        }
        try {
            if(keyFile.exists()) {
                LOGGER.info("Deleting key file: "+certFile.getAbsolutePath());
                Files.delete(keyFile.toPath());
            }
        } catch (IOException ex) {
            throw new CertificateStoreException("error deleting key file: "+keyFile.getAbsolutePath(),
                    ex);
        }
        try {
            if(directory.exists()) {
                LOGGER.info("Deleting cert directory: "+directory.getAbsolutePath());
                Files.delete(directory.toPath());
            }
        } catch (IOException ex) {
            throw new CertificateStoreException("error deleting certificate directory: "+directory.getAbsolutePath(),
                    ex);
        }
    }

}
