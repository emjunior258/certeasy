package org.certeasy.bouncycastle;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.certeasy.*;
import org.certeasy.KeyUsage;
import org.certeasy.certspec.*;
import org.certeasy.Certificate;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BouncyCastleCertificateGenerator implements CertificateGenerator {

    @Override
    public Certificate generate(CertificateSpec spec, Certificate authorityCertificate) {
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null");
        if(authorityCertificate==null)
            throw new IllegalArgumentException("authority certificate MUST not be null");
        X500Name issuerName = toX500Name(authorityCertificate.getSubject().getDistinguishedName());
        try {
            ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(authorityCertificate.getPrivateKey());
            return makeCertificate(spec, issuerName, makeKeyPair(spec), signer);
        }catch (OperatorCreationException ex){
            throw new CertGenerationException("error creating certificate content signer", ex);
        }
    }

    @Override
    public Certificate generate(CertificateAuthoritySpec spec) {
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null");
        X500Name issuerName = toX500Name(spec.getSubject().getDistinguishedName());
        KeyPair keyPair = makeKeyPair(spec);
        try {
            ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
            return makeCertificate(spec, issuerName, makeKeyPair(spec), signer);
        }catch (OperatorCreationException ex){
            throw new CertGenerationException("error creating certificate content signer", ex);
        }
    }


    private Certificate makeCertificate(CertificateSpec spec,X500Name issuerName, KeyPair keyPair, ContentSigner contentSigner){
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        X500Name subjectName = toX500Name(spec.getSubject().getDistinguishedName());
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuerName, serialNumber, Date.from(Instant.now()),
                Date.from(spec.getExpiryDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                subjectName, keyPair.getPublic());
        try {
            if (spec.isCertificateAuthority())
                builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
            if(spec.getSubject().hasAlternativeNames())
                builder.addExtension(Extension.subjectAlternativeName, false, toGeneralNames(spec.getSubject().getAlternativeNames()));
            builder.addExtension(Extension.keyUsage, true, makeKeyUsage(spec));
            X509CertificateHolder holder = builder.build(contentSigner);
            return new Certificate(spec, String.valueOf(holder.getSerialNumber()), keyPair.getPrivate(),
                    holder.getEncoded());
        }catch (CertIOException ex){
            throw new CertGenerationException("error adding extensions to certificate", ex);
        }catch (IOException ex){
            throw new CertGenerationException("error reading encoded certificate", ex);
        }
    }

    private GeneralNames toGeneralNames(Set<SubjectAlternativeName> sans){
        Set<GeneralName> names = new HashSet<>();
        sans.forEach(alt -> {
            switch (alt.type()){
                case EMAIL -> names.add(new GeneralName(GeneralName.rfc822Name, alt.value()));
                case DNS -> names.add(new GeneralName(GeneralName.dNSName, alt.value()));
                case OTHER_NAME -> names.add(new GeneralName(GeneralName.otherName, new DERPrintableString(alt.value())));
                case DIRECTORY_NAME -> names.add(new GeneralName(GeneralName.directoryName, alt.value()));
                case URI -> names.add(new GeneralName(GeneralName.uniformResourceIdentifier, alt.value()));
                case IP_ADDRESS -> names.add(new GeneralName(GeneralName.iPAddress, alt.value()));
            }
        });
        GeneralName[]  namesArray = names.toArray(new GeneralName[] {});
        return GeneralNames.getInstance(new DERSequence(namesArray));
    }

    private X500Name toX500Name(DistinguishedName distinguishedName){
        Collection<RDN> rdns = distinguishedName.relativeDistinguishedNames()
                .stream()
                .map(item -> new RDN(
                        new ASN1ObjectIdentifier(item.type().getOid()),
                        new DERPrintableString(item.value())))
                .collect(Collectors.toSet());
        RDN[] array = new RDN[rdns.size()];
        rdns.toArray(array);
        return new X500Name(array);
    }

    private KeyPair makeKeyPair(CertificateSpec spec){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(spec.getKeyStrength().getSize());
            return keyGen.generateKeyPair();
        }catch (NoSuchAlgorithmException ex){
            throw new CertGenerationException("RSA key pairs generator not found", ex);
        }
    }


    private org.bouncycastle.asn1.x509.KeyUsage makeKeyUsage(CertificateSpec spec){
        int keyUsageInt = 0;
        for(KeyUsage keyUsage: spec.getPublicKeyUsages()){
            switch (keyUsage){
                case DigitalSignature -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.digitalSignature;
                case CertificateSign -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.keyCertSign;
                case KeyEncipherment -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment;
                case NonRepudiation -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation;
                case DataEncipherment -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment;
                case SignCRL -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.cRLSign;
                case KeyAgreement -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.keyAgreement;
                case DecipherOnly -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.decipherOnly;
                case EncipherOnly -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.encipherOnly;
            }
        }
        return new org.bouncycastle.asn1.x509.KeyUsage(keyUsageInt);
    }

}
