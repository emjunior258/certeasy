package org.certeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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

    private CertificateSummary summary;

    public Certificate(String serial, DistinguishedName distinguishedName, DateRange validityPeriod, KeyStrength keyStrength, PrivateKey privateKey, byte[] derBytes,  Set<KeyUsage> keyUsages, ExtendedKeyUsages extendedKeyUsages){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(distinguishedName==null)
            throw new IllegalArgumentException("distinguishedName MUST not be null nor empty");
        if(validityPeriod==null)
            throw new IllegalArgumentException("ExtendedKeyUsages extendedKeyUsages, validityPeriod MUST not be null");
        if(keyStrength==null)
            throw new IllegalArgumentException("key strength MUST not be null");
        if(privateKey==null)
            throw new IllegalArgumentException("privateKey MUST not be null");
        if(keyUsages==null)
            throw new IllegalArgumentException("keyUsages MUST not be null");
        if(derBytes==null || derBytes.length==0)
            throw new IllegalArgumentException("derBytes array MUST not be null nor empty");
        this.serial = serial;
        this.distinguishedName = distinguishedName;
        this.validityPeriod = validityPeriod;
        this.keyStrength = keyStrength;
        this.keyUsages = Collections.unmodifiableSet(keyUsages);
        if(extendedKeyUsages!=null)
            this.extendedKeyUsages = extendedKeyUsages;
        this.privateKey = privateKey;
        this.derBytes = derBytes;
        this.makeSummary();
    }


    public Certificate(CertificateSpec spec, String serial, PrivateKey privateKey, byte[] derBytes){
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null nor empty");
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(privateKey==null)
            throw new IllegalArgumentException("key pair MUST not be null nor empty");
        if(derBytes==null)
            throw new IllegalArgumentException("derBytes array MUST not be null");
        this.distinguishedName = spec.getSubject().getDistinguishedName();
        this.validityPeriod = spec.getValidityPeriod();
        this.keyStrength = spec.getKeyStrength();
        this.keyUsages = spec.getKeyUsages();
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
            throw new CertEasyException("error writing encoded certificate to stream", ex);
        }
    }

    public Set<KeyUsage> getKeyUsages() {
        return keyUsages;
    }

    public Optional<ExtendedKeyUsages> getExtendedKeyUsages() {
        return Optional.ofNullable(extendedKeyUsages);
    }

    private void makeSummary(){
        this.summary = new CertificateSummary(this.distinguishedName.getCommonName(), serial,
                validityPeriod);
    }

    public Optional<ExtendedKeyUsages> getExtendedKeyUsage(){
        return Optional.ofNullable(extendedKeyUsages);
    }

    public CertificateSummary getSummary(){
        return this.summary;
    }

}
