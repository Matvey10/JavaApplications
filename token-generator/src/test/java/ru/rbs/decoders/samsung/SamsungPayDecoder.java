package ru.rbs.decoders.samsung;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.util.Base64URL;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.bpc.phoenix.model.SamsungPay;

import ru.bpc.service.CertificateService;
import ru.rbs.commons.encrypt.CryptoService;
import ru.rbs.tokengenerator.samsungpay.SamsungPayPaymentToken;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.security.Key;
import java.security.Security;
import java.util.List;

@Service
public class SamsungPayDecoder {

    private final static Logger logger = LoggerFactory.getLogger(SamsungPayDecoder.class);

    @Resource
    private CryptoService aesCryptUtil;
    @Resource
    private ObjectMapper objectMapper;

    public SamsungPayPaymentToken decode(String encodedPaymentToken, List<SamsungPay> samsungPays) {
        return samsungPays.stream()
                .map(samsungPay -> decodePaymentToken(encodedPaymentToken, samsungPay))
                .filter(SamsungPayPaymentToken::areMandatoryFieldsNotBlank)
                .findAny()
                .orElse(new SamsungPayPaymentToken());
    }

    private SamsungPayPaymentToken decodePaymentToken(String encodedPaymentToken, SamsungPay samsungPay) {
        try {
            String privateKeyStr = new String(aesCryptUtil.decrypt(Base64.decodeBase64(samsungPay.getPrivateKey())));
            return decodePaymentToken(encodedPaymentToken, privateKeyStr);
        } catch (Exception e) {
            logger.error("Error during samsung privateKey decrypting", e);
            return new SamsungPayPaymentToken();
        }
    }

    public SamsungPayPaymentToken decodePaymentToken(String encodedPaymentToken, String privateKeyStr) {
        try {
            Key key = CertificateService.getPrivateKeyFromString(privateKeyStr, "RSA");
            String decodedPaymentToken = decode(encodedPaymentToken, key);
            return objectMapper.readValue(decodedPaymentToken, SamsungPayPaymentToken.class);
        } catch (Exception e) {
            logger.error("Error during samsung payment token decoding", e);
            return new SamsungPayPaymentToken();
        }
    }

    public String decode(String payload, Key key) throws Exception {
        String[] jwe = payload.split("\\."); //JWE Compact Serialization
        byte[] encryptedKey = Base64.decodeBase64(jwe[1]);
        Base64URL iv = new Base64URL(jwe[2]);
        Base64URL cipherText = new Base64URL(jwe[3]);
        Base64URL authTag = new Base64URL(jwe[4]);
        //http://self-issued.info/docs/draft-jones-json-web-encryption-02.html

        Security.addProvider(new BouncyCastleProvider());

        //5 Decrypt the JWE Encrypted Key to produce the CEK.
        Cipher cekCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cekCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] cek = cekCipher.doFinal(encryptedKey);

        //6 Decrypt the binary representation of the JWE CipherText using the CEK.
        DirectDecrypter decrypter = new DirectDecrypter(cek);
        //JWEHeader jweHeader = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM);
        JWEHeader jweHeader = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM, //requires 128 bit key
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, 0, null, null, null, new Base64URL(""));

        byte[] b = decrypter.decrypt(jweHeader, null, iv, cipherText, authTag);
        return new String(b);
    }
}