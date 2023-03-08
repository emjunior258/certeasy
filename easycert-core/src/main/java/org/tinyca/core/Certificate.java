package org.tinyca.core;

import org.tinyca.core.certspec.CertificateAuthoritySubject;
import org.tinyca.core.certspec.EmployeeIdentitySubject;
import org.tinyca.core.certspec.PersonalIdentitySubject;
import org.tinyca.core.certspec.TLSServerSubject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * Represents an X509 V3 Certificate along with the respective private-key.
 */
public class Certificate {

    private final String serial;
    private final CertificateSubject subject;
    private final KeyStrength keyStrength;
    private final Set<KeyUsage> keyUsages;
    private final PrivateKey privateKey;
    private final byte[] derBytes;

    private final LocalDate expiryDate;
    private CertificateSummary summary;
    private CertificateType type;

    public Certificate(String serial, CertificateSubject subject, LocalDate expiryDate, KeyStrength keyStrength, Set<KeyUsage> keyUsages, PrivateKey privateKey, byte[] derBytes){
        if(serial==null || serial.isEmpty())
            throw new IllegalArgumentException("serial MUST not be null nor empty");
        if(subject==null)
            throw new IllegalArgumentException("subject MUST not be null nor empty");
        if(expiryDate==null)
            throw new IllegalArgumentException("expiry date MUST not be null nor empty");
        if(keyStrength==null)
            throw new IllegalArgumentException("key strength MUST not be null");
        if(keyUsages==null || keyUsages.isEmpty())
            throw new IllegalArgumentException("key usages set MUST not be null nor empty");
        if(privateKey==null)
            throw new IllegalArgumentException("privateKey MUST not be null");
        if(derBytes==null || derBytes.length==0)
            throw new IllegalArgumentException("derBytes array MUST not be null nor empty");
        this.serial = serial;
        this.subject = subject;
        this.expiryDate = expiryDate;
        this.keyStrength = keyStrength;
        this.keyUsages = Collections.unmodifiableSet(keyUsages);
        this.privateKey = privateKey;
        this.derBytes = derBytes;
        this.figureType();
        this.makeSummary();
    }



    private void figureType(){
        if(subject instanceof EmployeeIdentitySubject)
            this.type = CertificateType.Employee;
        else if(subject instanceof PersonalIdentitySubject)
            this.type = CertificateType.Personal;
        else if(subject instanceof TLSServerSubject)
            this.type = CertificateType.TLSServer;
        else if(subject instanceof CertificateAuthoritySubject)
            this.type = CertificateType.Authority;
        else this.type = CertificateType.Custom;
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
        this.subject = spec.getSubject();
        this.expiryDate = spec.getExpiryDate();
        this.keyStrength = spec.getKeyStrength();
        this.keyUsages = spec.getPublicKeyUsages();
        this.serial = serial;
        this.privateKey = privateKey;
        this.derBytes = derBytes;
        this.figureType();
        this.makeSummary();
    }

    public String getSerial() {
        return serial;
    }

    public CertificateSubject getSubject() {
        return subject;
    }

    public KeyStrength getKeyStrength() {
        return keyStrength;
    }

    public Set<KeyUsage> getKeyUsages() {
        return keyUsages;
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
            throw new TinyCaException("failed to create file: "+file.getAbsolutePath(), ex);
        }
        try(FileOutputStream stream = new FileOutputStream(file)){
            this.exportDER(stream);
        }catch(IOException ex){
            throw new TinyCaException("error creating file stream: "+file.getAbsolutePath(),
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
            throw new TinyCaException("error writing encoded certificate to stream", ex);
        }
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean hasExpired(){
        return LocalDate.now().isAfter(
                expiryDate);
    }

    private void makeSummary(){
        this.summary = new CertificateSummary(this.subject.getCommonName(), serial,type,
                hasExpired() ? CertificateStatus.Expired : CertificateStatus.Active);
    }

    public CertificateSummary getSummary(){
        return this.summary;
    }

}
