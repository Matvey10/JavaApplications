package ru.bpc.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
/**
 * copied from ru.bpc.phoenix.service.CertificateService
 */
public class CertificateService {
    public final static String CERTIFICATE_TYPE = "X.509";
    private final String passwordKeyStore;
    private final Resource keyStoreResource;
    private final CertificateFactory cf;

    public CertificateService(Resource keyStoreResource, String passwordKeyStore) throws CertificateException {
        this.keyStoreResource = keyStoreResource;
        this.passwordKeyStore = passwordKeyStore;
        cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
    }

    public static PrivateKey getPrivateKeyFromString(String key, String algorithm) throws IOException, GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll(" ", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance(algorithm, "BC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return kf.generatePrivate(keySpec);
    }

    public static String getPublicKeyHash(String key) {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        return Base64.encodeBase64String(DigestUtils.sha256(Base64.decodeBase64(publicKeyPEM))).replaceAll("\r\n", "");//удаляем \r\n так как apache base64 по умолчанию добавляет \r\n
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}
