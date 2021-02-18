package ru.rbs.decoders.apple;

import org.springframework.stereotype.Service;
import ru.rbs.decoders.apple.AbstractApplePayDecoder;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;
import ru.bpc.phoenix.model.ApplePay;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.util.Base64;

@Service
public class RsaApplePayDecoder extends AbstractApplePayDecoder {

    private final static IvParameterSpec IV = new IvParameterSpec(new byte[16]);

    @Override
    protected String decode(PrivateKey pk, ApplePay applePay, ApplePayPaymentToken applePayPaymentToken) throws Exception {
        Cipher decryptWrappedKeyCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA256AndMGF1Padding", "BC");
        decryptWrappedKeyCipher.init(Cipher.DECRYPT_MODE, pk);

        byte[] wrappedKeyDecoded = decryptWrappedKeyCipher.doFinal(Base64.getDecoder().decode(applePayPaymentToken.getHeader().getWrappedKey()));
        Cipher decryptDataCipher = Cipher.getInstance("AES/GCM/NoPadding");

        SecretKeySpec secretKeySpec = new SecretKeySpec(wrappedKeyDecoded, "AES");
        decryptDataCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IV);

        final byte[] decValue = decryptDataCipher.doFinal(Base64.getDecoder().decode(applePayPaymentToken.getData()));
        return new String(decValue);
    }

    @Override
    public ApplePayPaymentData decodeToken(String paymentToken, String merchantLogin) throws MerchantNotFoundException {
        return super.decodeToken(paymentToken, merchantLogin);
    }

    @Override
    public ApplePayCertificateAlgorithm getCertificateType() {
        return ApplePayCertificateAlgorithm.RSA_v1;
    }
}
