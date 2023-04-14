package org.certeasy.bouncycastle;

import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.certeasy.*;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CertificateDecoder {

    private X509CertificateHolder holder;

    CertificateDecoder(X509CertificateHolder holder){
        this.holder = holder;
    }

    String serial(){
        return this.holder.getSerialNumber().toString();
    }

    KeyStrength getKeyStrength() {
        try {
            SubjectPublicKeyInfo subjectPublicKeyInfo = holder.getSubjectPublicKeyInfo();
            AlgorithmIdentifier algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
            RSAPublicKey rsaPublicKey = RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            BigInteger modulus = rsaPublicKey.getModulus();
            int keySize = modulus.bitLength();
            for (KeyStrength option : KeyStrength.values()) {
                if (option.getSize() == keySize)
                    return option;
            }

            throw new BouncyCastleCoderException(String.format("unsupported certificate key size : %d bits",
                    keySize));

        }catch (IOException ex){
            throw new BouncyCastleCoderException("error parsing Public Key from certificate",
                    ex);
        }
    }

    DistinguishedName extractDistinguishedName() {
        X500Name x500Name = holder.getSubject();
        RDN[] rdns = x500Name.getRDNs();
        DistinguishedName.Builder builder = DistinguishedName.builder();
        for (RDN rdn : rdns) {
            String oid = rdn.getFirst().getType().getId();
            SubjectAttributeType type = SubjectAttributeType.fromOID(oid);
            String attributeValue = rdn.getFirst().getValue().toString();
            builder.append(new RelativeDistinguishedName(type, attributeValue));
        }
        return builder.build();
    }

    Set<org.certeasy.KeyUsage> extractKeyUsage() {
        Set<org.certeasy.KeyUsage> keyUsageSet = new HashSet<>();
        Extension keyUsageExtension = holder.getExtension(Extension.keyUsage);
        if (keyUsageExtension != null) {
            KeyUsage keyUsage = KeyUsage.getInstance(keyUsageExtension.getParsedValue());
            if(keyUsage.hasUsages(KeyUsage.cRLSign))
                keyUsageSet.add(org.certeasy.KeyUsage.SignCRL);
            if(keyUsage.hasUsages(KeyUsage.keyAgreement))
                keyUsageSet.add(org.certeasy.KeyUsage.KeyAgreement);
            if(keyUsage.hasUsages(KeyUsage.keyEncipherment))
                keyUsageSet.add(org.certeasy.KeyUsage.KeyEncipherment);
            if(keyUsage.hasUsages(KeyUsage.keyCertSign))
                keyUsageSet.add(org.certeasy.KeyUsage.CertificateSign);
            if(keyUsage.hasUsages(KeyUsage.digitalSignature))
                keyUsageSet.add(org.certeasy.KeyUsage.DigitalSignature);
            if(keyUsage.hasUsages(KeyUsage.decipherOnly))
                keyUsageSet.add(org.certeasy.KeyUsage.DecipherOnly);
            if(keyUsage.hasUsages(KeyUsage.encipherOnly))
                keyUsageSet.add(org.certeasy.KeyUsage.EncipherOnly);
            if(keyUsage.hasUsages(KeyUsage.dataEncipherment))
                keyUsageSet.add(org.certeasy.KeyUsage.DataEncipherment);
            if(keyUsage.hasUsages(KeyUsage.nonRepudiation))
                keyUsageSet.add(org.certeasy.KeyUsage.NonRepudiation);
        }
        return Collections.unmodifiableSet(keyUsageSet);
    }

    ExtendedKeyUsages extractExtendedKeyUsage(){
        Set<org.certeasy.ExtendedKeyUsage> extendedKeyUsageSet = new HashSet<>();
        Extension extendedKeyUsageExtension = holder.getExtension(Extension.extendedKeyUsage);
        ExtendedKeyUsage extendedKeyUsage = ExtendedKeyUsage.getInstance(extendedKeyUsageExtension.getParsedValue());
        for(KeyPurposeId keyPurposeId: extendedKeyUsage.getUsages()){
            String id = keyPurposeId.getId();
            if(KeyPurposeId.id_kp_clientAuth.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.TLS_WEB_CLIENT_AUTH);
            else if(KeyPurposeId.id_kp_serverAuth.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.TLS_WEB_SERVER_AUTH);
            else if(KeyPurposeId.id_kp_OCSPSigning.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.OCSP_SIGNING);
            else if(KeyPurposeId.id_kp_ipsecTunnel.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.IPSEC_TUNNEL);
            else if(KeyPurposeId.id_kp_ipsecUser.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.IPSEC_USER);
            else if(KeyPurposeId.id_kp_ipsecEndSystem.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.IPSEC_END_SYSTEM);
            else if(KeyPurposeId.id_kp_emailProtection.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.EMAIL_PROTECTION);
            else if(KeyPurposeId.id_kp_timeStamping.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.TIMESTAMPING);
            else if(KeyPurposeId.id_kp_codeSigning.getId().equals(id))
                extendedKeyUsageSet.add(org.certeasy.ExtendedKeyUsage.SIGN_CODE);
            else throw new BouncyCastleCoderException("Unknown Extended Key Usage with ID "+keyPurposeId.getId());
        }
        ExtendedKeyUsageEffect effect = extendedKeyUsageExtension.isCritical() ? ExtendedKeyUsageEffect.Enforce : ExtendedKeyUsageEffect.Info;
        return new ExtendedKeyUsages(extendedKeyUsageSet,
                effect);
    }

    DateRange getValidityPeriod(){
        LocalDate start = holder.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = holder.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return new DateRange(start, end);
    }

    byte[] getBytes() {
        try {
            return holder.getEncoded();
        } catch (IOException ex) {
            throw new BouncyCastleCoderException("failed to retrieve DER Encoded bytes",
                    ex);
        }
    }

}
