package ru.rbs.tokengenerator.samsungpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWECryptoParts;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.util.Base64URL;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class SamsungPayEncoder {
        public static String encode(String merchantPublicKey, SamsungPayPaymentToken paymentToken) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            String tokenAsJson = objectMapper.writeValueAsString(paymentToken);
            System.out.println(tokenAsJson);

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            SecretKey cek = kgen.generateKey();
            byte[] originalWrappedKey = cek.getEncoded();

            byte[] byteKey = Base64.getDecoder().decode(formatPublicKey(merchantPublicKey).getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(X509publicKey);

            JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM, //requires 128 bit key
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, 0, null, null, null, new Base64URL(""));

            Security.addProvider(new BouncyCastleProvider());
            Cipher cekCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
            cekCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] wrappedCek = cekCipher.doFinal(originalWrappedKey);

            DirectEncrypter directEncrypter = new DirectEncrypter(cek);
            JWECryptoParts jweCryptoParts = directEncrypter.encrypt(header, tokenAsJson.getBytes());

            JwePayload jwePayload = new JwePayload();
            jwePayload.setHeader(jweCryptoParts.getHeader().toBase64URL().toString());
            jwePayload.setEncryptedKey(Base64URL.encode(wrappedCek).toString());
            jwePayload.setIv(jweCryptoParts.getInitializationVector().toString());
            jwePayload.setCipherText(jweCryptoParts.getCipherText().toString());
            jwePayload.setAuthTag(jweCryptoParts.getAuthenticationTag().toString());
            String serializedJweObject = jwePayload.serialize();

            return serializedJweObject;
    }

    public static PrivateKey getPrivateKeyFromString(String key, String algorithm) throws GeneralSecurityException {
        String privateKeyPEM = stripPemPrivateKey(key);
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance(algorithm, "BC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return kf.generatePrivate(keySpec);
    }

    public static String stripPemPrivateKey(String key) {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll(" ", "");
        return privateKeyPEM;
    }

    protected static String formatPublicKey(String key) {
        key = key.replace("-----BEGIN PUBLIC KEY-----\n", "");
        key = key.replace("-----BEGIN PUBLIC KEY-----", "");
        key = key.replace("-----END PUBLIC KEY-----", "");
        return key;
    }
}
