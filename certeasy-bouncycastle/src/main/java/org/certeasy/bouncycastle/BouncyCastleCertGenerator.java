package org.certeasy.bouncycastle;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.certeasy.*;
import org.certeasy.BasicConstraints;
import org.certeasy.KeyUsage;
import org.certeasy.certspec.*;
import org.certeasy.Certificate;

import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BouncyCastleCertGenerator implements CertificateGenerator {

    public BouncyCastleCertGenerator() {
        BouncyCastleSecurityProvider.install();
    }

    @Override
    public Certificate generate(CertificateSpec spec, Certificate authorityCertificate) {
        if(spec==null)
            throw new IllegalArgumentException("spec MUST not be null");
        if(authorityCertificate==null)
            throw new IllegalArgumentException("authority certificate MUST not be null");
        X500Name issuerName = toX500Name(authorityCertificate.getDistinguishedName());
        try {
            ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(authorityCertificate.getPrivateKey());
            return makeCertificate(spec, issuerName, makeKeyPair(spec), signer);
        }catch (OperatorCreationException ex){
            throw new CertificateGeneratorException("error creating certificate content signer", ex);
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
            throw new CertificateGeneratorException("error creating certificate content signer", ex);
        }
    }

    private Certificate makeCertificate(CertificateSpec spec,X500Name issuerName, KeyPair keyPair, ContentSigner contentSigner){
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        X500Name subjectName = toX500Name(spec.getSubject().getDistinguishedName());

        Date validityStartDate = Date.from(spec.getValidityPeriod().start().atTime(0,0).atZone(ZoneId.systemDefault()).toInstant());
        Date validityEndDate = Date.from(spec.getValidityPeriod().start().atTime(23,59).atZone(ZoneId.systemDefault()).toInstant());

        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuerName, serialNumber, validityStartDate, validityEndDate,
                subjectName, keyPair.getPublic());

        BasicConstraints basicConstraints = spec.getBasicConstraints();

        try {
            if (basicConstraints.ca())
                builder.addExtension(Extension.basicConstraints, true, new org.bouncycastle.asn1.x509.BasicConstraints(basicConstraints.pathLength()));
            else builder.addExtension(Extension.basicConstraints, true, new org.bouncycastle.asn1.x509.BasicConstraints(false));
            if(spec.getSubject().hasAlternativeNames())
                builder.addExtension(Extension.subjectAlternativeName, false, toGeneralNames(spec.getSubject().getAlternativeNames()));
            builder.addExtension(Extension.keyUsage, true, makeKeyUsage(spec));
            if(spec.getExtendedKeyUsages().isPresent()){
                ExtendedKeyUsages extendedKeyUsageDef = spec.getExtendedKeyUsages().get();
                boolean critical  = ExtendedKeyUsageEffect.ENFORCE == extendedKeyUsageDef.effect();
                builder.addExtension(Extension.extendedKeyUsage, critical,  makeExtendedKeyUsage(spec));
            }
            X509CertificateHolder holder = builder.build(contentSigner);
            DistinguishedName issuerDN = DistinguishedName.builder()
                    .parse(issuerName.toString())
                    .build();
            return new Certificate(spec, String.valueOf(holder.getSerialNumber()), keyPair.getPrivate(),
                    holder.getEncoded(), issuerDN);
        }catch (CertIOException ex){
            throw new CertificateGeneratorException("error adding extensions to certificate", ex);
        }catch (IOException ex){
            throw new CertificateGeneratorException("error reading encoded certificate", ex);
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
            throw new CertificateGeneratorException("RSA key pairs generator not found", ex);
        }
    }

    private ExtendedKeyUsage makeExtendedKeyUsage(CertificateSpec spec){
        Set<KeyPurposeId> keyPurposeIdSet = new HashSet<>();
        ExtendedKeyUsages extendedKeyUsages = spec.getExtendedKeyUsages().get();
        for(org.certeasy.ExtendedKeyUsage item: extendedKeyUsages.usages()){
            keyPurposeIdSet.add(toKeyPurpose(item));
        }
        KeyPurposeId[] keyPurposeIdsArray = new KeyPurposeId[keyPurposeIdSet.size()];
        keyPurposeIdSet.toArray(keyPurposeIdsArray);
        return new ExtendedKeyUsage(keyPurposeIdsArray);
    }

    private KeyPurposeId toKeyPurpose(org.certeasy.ExtendedKeyUsage keyUsage){
        return switch (keyUsage) {
            case SIGN_CODE -> KeyPurposeId.id_kp_codeSigning;
            case EMAIL_PROTECTION -> KeyPurposeId.id_kp_emailProtection;
            case IPSEC_END_SYSTEM -> KeyPurposeId.id_kp_ipsecEndSystem;
            case IPSEC_TUNNEL -> KeyPurposeId.id_kp_ipsecTunnel;
            case IPSEC_USER -> KeyPurposeId.id_kp_ipsecUser;
            case OCSP_SIGNING -> KeyPurposeId.id_kp_OCSPSigning;
            case TIMESTAMPING -> KeyPurposeId.id_kp_timeStamping;
            case TLS_WEB_CLIENT_AUTH -> KeyPurposeId.id_kp_clientAuth;
            case TLS_WEB_SERVER_AUTH -> KeyPurposeId.id_kp_serverAuth;
        };
    }


    private org.bouncycastle.asn1.x509.KeyUsage makeKeyUsage(CertificateSpec spec){
        int keyUsageInt = 0;
        for(KeyUsage keyUsage: spec.getKeyUsages()){
            switch (keyUsage){
                case DIGITAL_SIGNATURE -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.digitalSignature;
                case CERTIFICATE_SIGN -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.keyCertSign;
                case KEY_ENCIPHERMENT -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment;
                case NON_REPUDIATION -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation;
                case DATA_ENCIPHERMENT -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment;
                case SIGN_CRL -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.cRLSign;
                case KEY_AGREEMENT -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.keyAgreement;
                case DECIPHER_ONLY -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.decipherOnly;
                case ENCIPHER_ONLY -> keyUsageInt += org.bouncycastle.asn1.x509.KeyUsage.encipherOnly;
            }
        }
        return new org.bouncycastle.asn1.x509.KeyUsage(keyUsageInt);
    }

}
