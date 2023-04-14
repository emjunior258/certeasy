package org.certeasy.bouncycastle;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.certeasy.Certificate;
import org.certeasy.PEMCoder;
import org.certeasy.PEMCoderException;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Set;

public class BouncyCastlePEMCoder implements PEMCoder  {


    public BouncyCastlePEMCoder(){
        BouncyCastleSecurityProvider.install();
    }

    @Override
    public Certificate decodeCertificate(String certPem, String privateKeyPem) throws PEMCoderException {
        if(certPem==null||certPem.isEmpty())
            throw new IllegalArgumentException("certPem MUST not be null nor empty");
        if(privateKeyPem==null||privateKeyPem.isEmpty())
            throw new IllegalArgumentException("privateKeyPem MUST not be null nor empty");
        PrivateKey privateKey = decodePrivateKey(privateKeyPem);
        CertificateDecoder decoder = decodeCert(certPem);
        return new Certificate(decoder.serial(),
                decoder.extractDistinguishedName(),
                decoder.getValidityPeriod(),
                decoder.getKeyStrength(), privateKey,
                decoder.getBytes(), decoder.extractKeyUsage(),
                decoder.extractExtendedKeyUsage());
    }

    private PrivateKey decodePrivateKey(String privateKey){
        Reader pemReader = new StringReader(privateKey);
        PEMParser parser = new PEMParser(pemReader);
        try {
            Object pemObj = parser.readObject();
            if (pemObj instanceof PrivateKeyInfo privateKeyInfo) {
                byte[] privateKeyBytes = privateKeyInfo.getEncoded();
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
                return keyFactory.generatePrivate(keySpec);
            } else throw new PEMCoderException("provided PEM content doesn't contain a privateKey");
        }catch (IOException | NoSuchAlgorithmException | NoSuchProviderException ex){
            throw new PEMCoderException("error decoding RSA private key", ex);
        }catch (InvalidKeySpecException ex){
            throw new PEMCoderException("provided PEM content is not a valid privateKey", ex);
        }
    }

    private CertificateDecoder decodeCert(String cert){
        Reader pemReader = new StringReader(cert);
        PEMParser parser = new PEMParser(pemReader);
        try {
            Object pemObj = parser.readObject();
            if (pemObj instanceof X509CertificateHolder certificateHolder) {
                return new CertificateDecoder(certificateHolder);
            } else throw new PEMCoderException("provided PEM content doesn't contain a certificate");
        }catch (IOException ex) {
            throw new PEMCoderException("error decoding certificate", ex);
        }
    }

    @Override
    public String encodePrivateKey(Certificate certificate) throws PEMCoderException {
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        try {
            StringWriter stringWriter = new StringWriter();
            JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
            pemWriter.writeObject(new JcaPKCS8Generator(certificate.getPrivateKey(), null));
            pemWriter.close();
            return stringWriter.toString();
        }catch (IOException ex){
            throw new PEMCoderException("error encoding privateKey to PEM",
                    ex);
        }
    }

    @Override
    public String encodeCert(Certificate certificate) throws PEMCoderException {
        if(certificate==null)
            throw new IllegalArgumentException("certificate MUST not be null");
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificate.getDERBytes()));
            StringWriter stringWriter = new StringWriter();
            JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
            pemWriter.writeObject(cert);
            pemWriter.close();
            return stringWriter.toString();
        }catch (IOException | CertificateException ex){
            throw new PEMCoderException("error encoding certificate to PEM",
                    ex);
        }
    }

    @Override
    public String encodeChain(Set<Certificate> chain) throws PEMCoderException {
        if(chain==null||chain.isEmpty())
            throw new IllegalArgumentException("chain MUST not be null nor empty");
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        try {
            for(Certificate certificate: chain){
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificate.getDERBytes()));
                pemWriter.writeObject(cert);
            }
            pemWriter.close();
            return stringWriter.toString();
        }catch (IOException | CertificateException ex){
            throw new PEMCoderException("error encoding certificate chain to PEM",
                    ex);
        }
    }
}
