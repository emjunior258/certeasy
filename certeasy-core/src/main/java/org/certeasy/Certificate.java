package org.certeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.*;

/**
 * Represents an X509 V3 Certificate along with the respective private-key.
 */
public class Certificate {

    private final String serial;
    private final KeyStrength keyStrength;
    private final PrivateKey privateKey;
    private final byte[] derBytes;
    private final DateRange validityPeriod;
    private final DistinguishedName distinguishedName;

    private Set<KeyUsage> keyUsages;

    private ExtendedKeyUsages extendedKeyUsages;

    private BasicConstraints basicConstraints;

    private Set<SubjectAlternativeName> subjectAltNames = new HashSet<>();

    private CertificateSummary summary;

    private DistinguishedName issuerName;

    public Certificate(String serial, DistinguishedName distinguishedName, DateRange validityPeriod, KeyStrength keyStrength, BasicConstraints basicConstraints, PrivateKey privateKey, byte[] derBytes, Set<SubjectAlternativeName> subjectAltNames, Set<KeyUsage> keyUsages, DistinguishedName issuerDistinguishedName, ExtendedKeyUsages extendedKeyUsages){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(distinguishedName==null)
            throw new IllegalArgumentException("distinguishedName MUST not be null nor empty");
        if(validityPeriod==null)
            throw new IllegalArgumentException("ExtendedKeyUsages usages, validityPeriod MUST not be null");
        if(keyStrength==null)
            throw new IllegalArgumentException("key strength MUST not be null");
        if(basicConstraints ==null)
            throw new IllegalArgumentException("basicConstraint MUST not be null");
        if(privateKey==null)
            throw new IllegalArgumentException("privateKey MUST not be null");
        if(keyUsages==null)
            throw new IllegalArgumentException("keyUsages MUST not be null");
        if(derBytes==null || derBytes.length==0)
            throw new IllegalArgumentException("derBytes array MUST not be null nor empty");
        if(issuerDistinguishedName==null)
            throw new IllegalArgumentException("issuerDistinguishedName MUST not be null");
        this.serial = serial;
        this.distinguishedName = distinguishedName;
        this.issuerName = issuerDistinguishedName;
        this.validityPeriod = validityPeriod;
        this.keyStrength = keyStrength;
        this.keyUsages = Collections.unmodifiableSet(keyUsages);
        this.basicConstraints = basicConstraints;
        if(subjectAltNames != null)
            this.subjectAltNames = subjectAltNames;
        if(extendedKeyUsages!=null)
            this.extendedKeyUsages = extendedKeyUsages;
        this.privateKey = privateKey;
        this.derBytes = derBytes;
        this.makeSummary();
    }

    public Certificate(CertificateSpec spec, String serial, PrivateKey privateKey, byte[] derBytes, DistinguishedName issuerDistinguishedName){
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null nor empty");
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(privateKey==null)
            throw new IllegalArgumentException("key pair MUST not be null nor empty");
        if(derBytes==null)
            throw new IllegalArgumentException("derBytes array MUST not be null");
        if(issuerDistinguishedName==null)
            throw new IllegalArgumentException("issuerDistinguishedName MUST not be null");
        this.distinguishedName = spec.getSubject().getDistinguishedName();
        this.issuerName = issuerDistinguishedName;
        this.validityPeriod = spec.getValidityPeriod();
        this.keyStrength = spec.getKeyStrength();
        this.keyUsages = spec.getKeyUsages();
        this.basicConstraints = spec.getBasicConstraints();
        if(spec.getExtendedKeyUsages().isPresent())
            this.extendedKeyUsages = spec.getExtendedKeyUsages().get();
        this.serial = serial;
        this.privateKey = privateKey;
        this.derBytes = derBytes;
        this.makeSummary();
    }

    public String getSerial() {
        return serial;
    }

    public KeyStrength getKeyStrength() {
        return keyStrength;
    }

    public DateRange getValidityPeriod() {
        return validityPeriod;
    }

    public DistinguishedName getDistinguishedName() {
        return distinguishedName;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public byte[] getDERBytes() {
        return derBytes;
    }

    public boolean exportDER(File file){
        if(file==null)
            throw new IllegalArgumentException("file MUST not be null");
        if(file.isDirectory())
            throw new IllegalArgumentException("file MUST not be a directory: "+ file.getAbsolutePath());
        try {
            file.createNewFile();
        } catch (IOException ex) {
            throw new CertEasyException("failed to create file: "+file.getAbsolutePath(), ex);
        }
        try(FileOutputStream stream = new FileOutputStream(file)){
            this.exportDER(stream);
        }catch(IOException ex){
            throw new CertEasyException("error creating file stream: "+file.getAbsolutePath(),
                    ex);
        }
        return true;
    }

    public void exportDER(OutputStream stream){
        if(stream==null)
            throw new IllegalArgumentException("stream MUST not be null");
        try {
            stream.write(derBytes);
        } catch (IOException ex) {
            throw new CertEasyException("error writing DER encoded certificate to stream", ex);
        }
    }

    public Set<KeyUsage> getKeyUsages() {
        return keyUsages;
    }

    public Optional<ExtendedKeyUsages> getExtendedKeyUsages() {
        return Optional.ofNullable(extendedKeyUsages);
    }

    public Set<SubjectAlternativeName> getSubjectAltNames() {
        return Collections.unmodifiableSet(subjectAltNames);
    }

    private void makeSummary(){
        this.summary = new CertificateSummary(this.distinguishedName.getCommonName(), serial,
                validityPeriod);
    }

    public CertificateSummary getSummary(){
        return this.summary;
    }

    public BasicConstraints getBasicConstraints() {
        return basicConstraints;
    }

    public DistinguishedName getIssuerName() {
        return this.issuerName;
    }

    public boolean isSelfSignedCA(){
        return this.issuerName.equals(distinguishedName) && this.basicConstraints.ca();
    }

    public boolean isCA(){
        return this.basicConstraints.ca();
    }

}
