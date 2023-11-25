package org.certeasy.bouncycastle;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.util.encoders.DecoderException;
import org.certeasy.*;

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

    private static final IllegalCertPemException ILLEGAL_CERT_PEM_EXCEPTION = new IllegalCertPemException("cert is not valid PEM encoded certificate");
    private static final IllegalPrivateKeyPemException ILLEGAL_PRIVATE_KEY_PEM_EXCEPTION = new IllegalPrivateKeyPemException("key is not valid PEM encoded privateKey");

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
                decoder.getKeyStrength(),
                decoder.extractBasicConstraints(),
                privateKey,
                decoder.getBytes(),
                decoder.extractSubjectAltNames(),
                decoder.extractKeyUsage(),
                decoder.extractIssuer(),
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
            } else throw new IllegalPrivateKeyPemException("provided PEM content doesn't contain a privateKey");
        }catch (InvalidKeySpecException | DecoderException | PEMException ex){
            throw ILLEGAL_PRIVATE_KEY_PEM_EXCEPTION;

        }catch (IOException | NoSuchAlgorithmException | NoSuchProviderException ex){
            throw new PEMCoderException("error decoding RSA private key", ex);
        }
    }

    private CertificateDecoder decodeCert(String cert){
        Reader pemReader = new StringReader(cert);
        PEMParser parser = new PEMParser(pemReader);
        try {
            Object pemObj = parser.readObject();
            if (pemObj instanceof X509CertificateHolder certificateHolder) {
                return new CertificateDecoder(certificateHolder);
            } else throw ILLEGAL_CERT_PEM_EXCEPTION;
        }catch (DecoderException | PEMException ex){
            throw ILLEGAL_CERT_PEM_EXCEPTION;
        }catch (IOException ex) {
            throw new BouncyCastleCoderException("error decoding certificate",
                    ex);
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
