package ru.rbs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWECryptoParts;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.util.Base64URL;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.bpc.phoenix.model.SamsungPay;
import ru.rbs.decoders.samsung.SamsungPayDecoder;
import ru.rbs.tokengenerator.dao.ApplePayDao;
import ru.rbs.tokengenerator.dao.MerchantDao;
import ru.rbs.tokengenerator.dao.SamsungPayDao;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.model.SimpleMerchant;
import ru.rbs.tokengenerator.samsungpay.JwePayload;
import ru.rbs.tokengenerator.samsungpay.SamsungPayDummyConstant;
import ru.rbs.tokengenerator.samsungpay.SamsungPayEncoder;
import ru.rbs.tokengenerator.samsungpay.SamsungPayPaymentToken;
import ru.rbs.tokengenerator.web.SamsungPayTokenRequest;
import ru.rbs.tokengenerator.web.controller.SamsungPayTokenGeneratorController;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.rbs.tokengenerator.samsungpay.SamsungPayDummyConstant.UTC;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestMainConfiguration.class)
@TestPropertySource(properties = {
        "data.scripts:classpath:data/bpcmerchantdetails.sql,classpath:data/vendor_pay.sql,classpath:data/samsung_pay.sql"
})
public class SamsungPayTest {
    @Autowired
    SamsungPayTokenGeneratorController controller;
    @Resource
    private WebApplicationContext context;
    private final static String merchant_login = "SamsungPayTest";
    private final static long amount = 99099;
    private final static String merchantPublicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgqGSz11Ng7RU27hO0HgfzoKl5qPelTT94+jlH5eEho0gRAqGcykgHRNtWXWV16qpooqQkXd6fpIHSpaAQ2vwW0Y1zJc7xjiWkY6bVs1TgROrJpHNbq3yIo0mpgDvMCVLfxAUt5KWg93B4LCNH5szi5BoKySaI7xSV3vIccxET1QFA+a1aMifEPU8DEAYr//sG0sptinQ57mbp6t+VhOC8LzgZr9Ut7XewqgYspwQd+ZsMA6rqP9UhgaUhE/wH8pl3o6kfuTtbU+ton4N0p6d03ecn2QYbf3rn3YGPkXbL85mhS4Dap1GNfPfqk1+54pjj5Q0XAVmRA6hRd8oNqww/QIDAQAB-----END PUBLIC KEY-----";
    private final static String merchantPrivateKey = "-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCoZLPXU2DtFTbuE7QeB/OgqXmo96VNP3j6OUfl4SGjSBECoZzKSAdE21ZdZXXqqmiipCRd3p+kgdKloBDa/BbRjXMlzvGOJaRjptWzVOBE6smkc1urfIijSamAO8wJUt/EBS3kpaD3cHgsI0fmzOLkGgrJJojvFJXe8hxzERPVAUD5rVoyJ8Q9TwMQBiv/+wbSym2KdDnuZunq35WE4LwvOBmv1S3td7CqBiynBB35mwwDquo/1SGBpSET/AfymXejqR+5O1tT62ifg3Snp3Td5yfZBht/eufdgY+RdsvzmaFLgNqnUY189+qTX7nimOPlDRcBWZEDqFF3yg2rDD9AgMBAAECggEASNzssYN+eAb/CitssrU+MIcJGHOw9JLBRv2hx5RRFGflZm5kfhmjUvSCGtMOiGQmCIa3ipwVFEJJlN4Lnw+22oJ4Aa0dZzwrJ/deR/VxLV8WKQZMiv61P3Zx50hAIh+7U51ZoOc/0z+/I5/2p3l/L8qVf/MDkwk4+JkPvgHM3F3ZxWX+Srt/xP/o7qGepytxENVBmrEiszotFXUnCPgAj1DoU1SNmpI+KHOmcLKRUPA0w6cbwlfMIwFkIURD9xxRVOXltI92y4V1SQP/vev2/ytJPh7I+M8K7e1bqcrVgslg/wQp2W/a4NxxlINESnco+l3scEwGJvUjWutNRIy0wQKBgQDAa7KTPKmlazl4K0BTGKqqUxgU53yodR1za4+VDsmi94x9THZtWhS6/UxUakY6mikIeUbx3WM8W4hIeBAr1hH7d51kS9cgYLZ5OQ+pkxaAEMiCUlovEiQrG7ywYDpx03Qz0JWahuxKvRY1hVIsXauSbnYrNapa1JLo5iN84F2ljQKBgQCty0d8P6zcxuVF9WNufpcbG9X6eAc+ZJyPqOpN9HDrtVw6tPUFQuR732XCzzkLnmgdQZ5R/dbjsyYnjo3JhEWNzvrBv1b5b2zR9VL2Q6plsFkH/B/J6uH7h7+u4h+5vtoz/cNanOCqHlre66QbSuiwMJvly+D2cnqFPlvGHkLFMQKBgC3rhbpNh87KP/TFnZw3DsOHAQA8Hiu1h0m8KY/Q753MpeJhFQITtU7KlCSxGAF0OMak6qFdzAjjbyqcPH+5nqD8crs0vioj6mgTjgFR8NSvLKqtLIh/M9J11BnjXQ88/0Y+SPWPrWRIq7wqDOo5kHBpnBnDvNvgTw71nRJ4GOEhAoGBAJ1JS/j258sndnhKDSMDDl5KYu27k1K0W7zocqT6hPfNuxqmitmoIIC7Pa2BPW92fWN873vk74ZDrFRAf7HRM+GTYP3X3S8B2Mpr7BlUQ/KkFu9TDrH/EWZDBQeRPoUsZO6IyzRGpk1QC42UVYEd2lkapPzj90UTdi8AsK4s1OEBAoGAEj95POAlY7O2rItWUi7RLmWQguw3jjZt9z01Gdsvp/Vyn7jKTc8m4MIkZGpWhePSs8qeYspTkSwcacAqWCLgO3COTMrbUhzyMbAYGYxZBZEANa2khOG4pRJCZqHDhUOT9uJFXOdKZOC/daz1esGmet/cSflNTwRNPplkTDPl3qs=-----END PRIVATE KEY-----";
    private final String URL = "/rest/generateSamsungPayPaymentToken.do";
    private String tokenRequest;
    private String badTokenRequest;
    private MockMvc mockMvc;

    @Autowired
    MerchantDao merchantDao;
    @Autowired
    SamsungPayDao samsungPayDao;
    @Autowired
    ApplePayDao applePayDao;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Before
    public void createTokenRequest() throws JsonProcessingException {
        SamsungPayTokenRequest tokenRequest = new SamsungPayTokenRequest();
        tokenRequest.setAmount(amount);
        tokenRequest.setMerchantLogin(merchant_login);
        tokenRequest.setPublicKey(merchantPublicKey);
        tokenRequest.setTokenPAN(SamsungPayDummyConstant.TOKEN_PAN);
        tokenRequest.setCurrencyCode(SamsungPayDummyConstant.CURRENCY_CODE);
        tokenRequest.setTokenPanExpiration(SamsungPayDummyConstant.TOKEN_PAN_EXPIRATION);
        tokenRequest.setUtc(UTC);
        ObjectMapper objectMapper = new ObjectMapper();
        this.tokenRequest = objectMapper.writeValueAsString(tokenRequest);
    }

    @Before
    public void createBadTokenRequest() throws JsonProcessingException {
        SamsungPayTokenRequest tokenRequest = new SamsungPayTokenRequest();
        tokenRequest.setAmount(Long.valueOf(0));
        tokenRequest.setMerchantLogin(merchant_login);
        tokenRequest.setPublicKey(merchantPublicKey);
        tokenRequest.setTokenPAN(SamsungPayDummyConstant.TOKEN_PAN);
        tokenRequest.setCurrencyCode(SamsungPayDummyConstant.CURRENCY_CODE);
        tokenRequest.setTokenPanExpiration(SamsungPayDummyConstant.TOKEN_PAN_EXPIRATION);
        tokenRequest.setUtc(UTC);
        ObjectMapper objectMapper = new ObjectMapper();
        this.badTokenRequest = objectMapper.writeValueAsString(tokenRequest);
    }

    @Test
    public void samsungPayDaoTest() throws MerchantNotFoundException {
        SimpleMerchant merchant = merchantDao.findByMerchantLogin(merchant_login);
        List<SamsungPay> samsungPays = samsungPayDao.findByMerchantId(merchant.getMerchantId());
        Assert.assertNotNull(samsungPays);
    }

    @Test
    public void generateTokenTest() throws Exception {
        this.mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tokenRequest))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void invalidRequestTest() throws Exception {
        this.mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badTokenRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void SamsungPayEncoderTestWithRealKeys() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        SamsungPayPaymentToken token = new SamsungPayPaymentToken();
        token.setAmount(990099);
        token.setCryptogram(SamsungPayDummyConstant.CRYPTOGRAM);
        token.setEciIndicator(SamsungPayDummyConstant.ECI_INDICATOR);
        token.setTokenPAN(SamsungPayDummyConstant.TOKEN_PAN);
        token.setCurrencyCode(SamsungPayDummyConstant.CURRENCY_CODE);
        token.setTokenPanExpiration(SamsungPayDummyConstant.TOKEN_PAN_EXPIRATION);
        token.setUtc(UTC);
        String tokenAsJson = objectMapper.writeValueAsString(token);

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey cek = kgen.generateKey();
        byte[] originalWrappedKey = cek.getEncoded();

        byte[] byteKey = Base64.decodeBase64(formatPublicKey(merchantPublicKey).getBytes());
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(X509publicKey);

        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM,
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

        PrivateKey privateKey = SamsungPayEncoder.getPrivateKeyFromString(merchantPrivateKey, "RSA");

        SamsungPayDecoder samsungPayDecoder = new SamsungPayDecoder();
        String decodedPayload = samsungPayDecoder.decode(serializedJweObject, privateKey);
        SamsungPayPaymentToken resultToken = objectMapper.readValue(decodedPayload, SamsungPayPaymentToken.class);
        Assert.assertEquals(tokenAsJson, decodedPayload);
    }

    @Test
    public void SamsungPayEncoderTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SamsungPayPaymentToken token = new SamsungPayPaymentToken();
        token.setAmount(100);
        token.setCryptogram(SamsungPayDummyConstant.CRYPTOGRAM);
        token.setEciIndicator(SamsungPayDummyConstant.ECI_INDICATOR);
        token.setTokenPAN(SamsungPayDummyConstant.TOKEN_PAN);
        token.setCurrencyCode(SamsungPayDummyConstant.CURRENCY_CODE);
        token.setTokenPanExpiration(SamsungPayDummyConstant.TOKEN_PAN_EXPIRATION);
        token.setUtc(UTC);
        String tokenAsJson = objectMapper.writeValueAsString(token);
        System.out.println(tokenAsJson);

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey cek = kgen.generateKey();
        byte[] originalWrappedKey = cek.getEncoded();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

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

        SamsungPayDecoder samsungPayDecoder = new SamsungPayDecoder();
        String decodedPayload = samsungPayDecoder.decode(serializedJweObject, privateKey);
        SamsungPayPaymentToken resultToken = objectMapper.readValue(decodedPayload, SamsungPayPaymentToken.class);
        System.out.println(resultToken.toString());
        Assert.assertEquals(tokenAsJson, decodedPayload);
    }

    public String formatPublicKey(String key) {
        key = key.replace("-----BEGIN PUBLIC KEY-----\n", "");
        key = key.replace("-----BEGIN PUBLIC KEY-----", "");
        key = key.replace("-----END PUBLIC KEY-----", "");
        return key;
    }
}
