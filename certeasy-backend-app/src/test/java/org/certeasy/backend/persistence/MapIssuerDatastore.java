package org.certeasy.backend.persistence;

import org.certeasy.CertEasyContext;
import org.certeasy.Certificate;

import java.util.*;
import java.util.stream.Collectors;

public class MapIssuerDatastore implements IssuerDatastore {

    private Map<String, StoredCert> certificateMap = new HashMap<>();

    private String issuerCertSerial;
    private CertEasyContext context;

    public MapIssuerDatastore(CertEasyContext context){
        this.context = context;
    }

    @Override
    public void put(Certificate certificate) throws IssuerDatastoreException {
        String certPem = context.pemCoder().encodeCert(certificate);
        String keyPem = context.pemCoder().encodePrivateKey(certificate);
        this.certificateMap.put(certificate.getSerial(), new MemoryStoredCert(certificate, certPem, keyPem));
    }

    @Override
    public Collection<StoredCert> listStored() throws IssuerDatastoreException {
        return this.certificateMap.values();
    }

    @Override
    public Optional<StoredCert> getCert(String serial) throws IssuerDatastoreException {
        return Optional.ofNullable(this.certificateMap.get(serial));
    }

    @Override
    public void deleteCert(String serial) throws IssuerDatastoreException {
        this.certificateMap.remove(serial);
    }

    @Override
    public Optional<String> getIssuerCertSerial() throws IssuerDatastoreException {
        return Optional.ofNullable(this.issuerCertSerial);
    }

    @Override
    public void putIssuerCertSerial(String serial) throws IssuerDatastoreException {
        this.issuerCertSerial = serial;
    }

    @Override
    public void purge() throws IssuerDatastoreException {
        this.certificateMap.clear();
    }
}
