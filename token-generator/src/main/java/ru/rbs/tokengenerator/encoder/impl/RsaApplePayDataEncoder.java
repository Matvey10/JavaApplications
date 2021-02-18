package ru.rbs.tokengenerator.encoder.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.bpc.phoenix.model.ApplePay;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class RsaApplePayDataEncoder extends AbstractApplePayDataEncoder {

    private final static IvParameterSpec IV = new IvParameterSpec(new byte[16]);

    @Override
    protected String encode(PrivateKey pk, ApplePay applePay, ApplePayPaymentToken applePayPaymentToken, ApplePayPaymentData applePayPaymentData) throws Exception {

        // generating symmetric key - случайный сеансовый ключ
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        byte[] originalWrappedKey = skey.getEncoded();

        //getting merchant public key - взять публичный ключ мерчанта
        byte[] byteKey = Base64.getDecoder().decode(formatPublicKey(applePay.getPublicKey()).getBytes());
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(X509publicKey);

        //encrypting symmetric key with public merchant key -
        //зашифровать сеансовый ключ с использованием открытого ключа мерчанта
        Cipher encryptWrappedKeyCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA256AndMGF1Padding", "BC");
        encryptWrappedKeyCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        final byte[] encodedWrappedKey = encryptWrappedKeyCipher.doFinal(originalWrappedKey);
        applePayPaymentToken.getHeader().setWrappedKey(new String(Base64.getEncoder().encode(encodedWrappedKey)));

        //encrypting applePayPaymentData with symmetric key -
        Cipher encryptDataCipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(originalWrappedKey, "AES");
        encryptDataCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IV);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] encodedData = encryptDataCipher.doFinal(objectMapper.writeValueAsBytes(applePayPaymentData));

        return new String(Base64.getEncoder().encode(encodedData));

    }

    @Override
    public ApplePayCertificateAlgorithm getCertificateType() {
        return ApplePayCertificateAlgorithm.RSA_v1;
    }

    @Override
    protected boolean isValidate(ApplePay applePay) {
        return ApplePayCertificateAlgorithm.RSA_v1.getAlgorithm().equalsIgnoreCase(applePay.getAlgorithm());
    }
}
