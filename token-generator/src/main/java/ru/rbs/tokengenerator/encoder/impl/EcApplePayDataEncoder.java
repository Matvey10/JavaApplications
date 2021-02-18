package ru.rbs.tokengenerator.encoder.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Bytes;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.stereotype.Service;
import ru.bpc.phoenix.model.ApplePay;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;

import javax.crypto.KeyAgreement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class EcApplePayDataEncoder extends AbstractApplePayDataEncoder {


    @Override
    protected String encode(PrivateKey pk, ApplePay applePay, ApplePayPaymentToken applePayPaymentToken, ApplePayPaymentData applePayPaymentData) throws Exception {
        String ephemeralPublicKey = formatPublicKey(applePay.getPublicKey());
        applePayPaymentToken.getHeader().setEphemeralPublicKey(ephemeralPublicKey);

        //getting public key
        X509EncodedKeySpec ks = new X509EncodedKeySpec(Base64.getDecoder().decode(ephemeralPublicKey));
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        ECPublicKey publicKey = (ECPublicKey) kf.generatePublic(ks);

        //generating secret //почему и приватный и публичный ключ берутся от одной стороны?
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
        keyAgreement.init(pk);
        keyAgreement.doPhase(publicKey, true);
        byte[] sharedSecret = keyAgreement.generateSecret();

        byte[] derivedKey = concatKDF(sharedSecret,
                Bytes.concat(new byte[]{0x0D}, "id-aes256-GCM".getBytes("ASCII")),
                "Apple".getBytes("ASCII"),
                DigestUtils.sha256(applePay.getMerchantAppleId().getBytes()));

        KeyParameter keyParam = new KeyParameter(derivedKey, 0, 32);
        CipherParameters params = new ParametersWithIV(keyParam, new byte[16]);

        //encrypting applePayPaymentData
        GCMBlockCipher cipher = new GCMBlockCipher(new AESFastEngine());
        cipher.reset();
        cipher.init(true, params);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = objectMapper.writeValueAsBytes(applePayPaymentData);
        byte[] encryptData = new byte[cipher.getOutputSize(data.length)];
        int len = cipher.processBytes(data, 0, data.length, encryptData, 0);
        cipher.doFinal(encryptData, len);

        return new String(Base64.getEncoder().encode(encryptData));
    }

    @Override
    protected boolean isValidate(ApplePay applePay) {
        return ApplePayCertificateAlgorithm.EC_v1.getAlgorithm().equalsIgnoreCase(applePay.getAlgorithm());
    }

    @Override
    public ApplePayCertificateAlgorithm getCertificateType() {
        return ApplePayCertificateAlgorithm.EC_v1;
    }

    private byte[] concatKDF(byte[] z, byte[] algorithmID, byte[] partyUInfo, byte[] partyVInfo) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if (algorithmID == null || partyUInfo == null || partyVInfo == null) {
            throw new NullPointerException("Required parameter is null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(algorithmID);
            baos.write(partyUInfo);
            baos.write(partyVInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] otherInfo = baos.toByteArray();
        return concatKDF(md, z, 32, otherInfo);
    }


    private byte[] concatKDF(MessageDigest md, byte[] z, int keyDataLen, byte[] otherInfo) {
        byte[] key = new byte[keyDataLen];

        int hashLen = md.getDigestLength();
        int reps = keyDataLen / hashLen;

        int counter = 1;
        byte[] counterInBytes = intToFourBytes(counter);

        if ((counterInBytes.length + z.length + otherInfo.length) * 8 > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Key derivation failed");
        }

        for (int i = 0; i <= reps; i++) {
            md.reset();
            md.update(intToFourBytes(i + 1));
            md.update(z);
            md.update(otherInfo);

            byte[] hash = md.digest();
            if (i < reps) {
                System.arraycopy(hash, 0, key, hashLen * i, hashLen);
            } else {
                System.arraycopy(hash, 0, key, hashLen * i, keyDataLen % hashLen);
            }
        }
        return key;
    }

    private byte[] intToFourBytes(int i) {
        byte[] res = new byte[4];
        res[0] = (byte) (i >>> 24);
        res[1] = (byte) ((i >>> 16) & 0xFF);
        res[2] = (byte) ((i >>> 8) & 0xFF);
        res[3] = (byte) (i & 0xFF);
        return res;
    }
}
