package ru.rbs.tokengenerator.service;

import com.google.crypto.tink.apps.paymentmethodtoken.PaymentMethodTokenSender;
import io.spring.gradle.dependencymanagement.org.codehaus.plexus.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.applepay.ApplePayDummyConstant;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;
import ru.rbs.tokengenerator.dao.ApplePayDao;
import ru.rbs.tokengenerator.dao.MerchantDao;
import ru.rbs.tokengenerator.encoder.ApplePayDataEncoder;
import ru.rbs.tokengenerator.encoder.ApplePayDataEncoderProvider;
import ru.rbs.tokengenerator.encoder.ApplePayPaymentTokenEncoder;
import ru.rbs.tokengenerator.encoder.impl.AbstractApplePayDataEncoder;
import ru.rbs.tokengenerator.exception.ApplePayNotFoundException;
import ru.rbs.tokengenerator.exception.ApplePayPaymentDataValidationException;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.exception.RequestParamException;
import ru.rbs.tokengenerator.model.SimpleMerchant;
import ru.rbs.tokengenerator.web.GooglePayTokenRequest;
import ru.rbs.tokengenerator.web.TokenRequest;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;

@Service
public class EncodingService {

    private final static Logger logger = LoggerFactory.getLogger(AbstractApplePayDataEncoder.class);

    @Resource
    private ApplePayDataEncoderProvider applePayDataEncoderProvider;
    @Resource
    private ApplePayDao applePayDao;
    @Resource
    private MerchantDao merchantDao;
    @Resource
    private ApplePayPaymentTokenEncoder applePayPaymentTokenEncoder;

    public String encodingRequestDataToPaymentToken(TokenRequest tokenRequest) throws ApplePayPaymentDataValidationException, MerchantNotFoundException, RequestParamException, ApplePayNotFoundException {
        logger.debug("Encoding started...");

        ApplePayPaymentData applePayPaymentData = mappingTokenRequest(tokenRequest);

        ApplePayCertificateAlgorithm applePayCertificateAlgorithm = ApplePayCertificateAlgorithm.valueOf(tokenRequest.getAlgorithm());
        ApplePayDataEncoder applePayDataEncoder = applePayDataEncoderProvider.get(applePayCertificateAlgorithm);

        SimpleMerchant merchant = merchantDao.findByMerchantLogin(tokenRequest.getMerchantLogin());
        if (StringUtils.isBlank(applePayPaymentData.getCurrencyCode())) {
            if (merchant.getDefaultCurrency() == null) {
                throw new RequestParamException(String.format("Parameter [currency] is missing and defaultCurrency for merchant [%s] is not defined", merchant.getMerchantLogin()));
            }
            applePayPaymentData.setCurrencyCode(merchant.getDefaultCurrency().getCurrency());
        }
        if (!applePayPaymentData.isValid()) {
            logger.error("Invalid ApplePay Payment Data [{}]", applePayPaymentData);
            throw new ApplePayPaymentDataValidationException("Invalid ApplePay Payment Data");
        }

        ApplePayPaymentToken applePayPaymentToken = applePayDataEncoder.encode(applePayDao.findByMerchantId(Long.valueOf(merchant.getMerchantId())), applePayPaymentData);

        return applePayPaymentTokenEncoder.encodeBase64(applePayPaymentToken);
    }

    public String generateGooglePayToken(GooglePayTokenRequest tokenRequest, String payload) {
        try {
            PaymentMethodTokenSender encryptor = new PaymentMethodTokenSender.Builder()
                    .protocolVersion("ECv1")
                    .recipientId(tokenRequest.getRecipientId())
                    .senderId("Google")
                    .senderSigningKey(tokenRequest.getSigningPrivateKey())
                    .recipientPublicKey(tokenRequest.getEncryptionPublicKey())
                    .build();
            return encryptor.seal(payload);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Cannot create token", e);
        }
    }


    private ApplePayPaymentData mappingTokenRequest(TokenRequest tokenRequest) {
        ApplePayPaymentData data = new ApplePayPaymentData();

        data.setApplicationPrimaryAccountNumber(tokenRequest.getPan());
        data.setTransactionAmount(tokenRequest.getAmount());
        data.setApplicationExpirationDate(tokenRequest.getExpiry());
        data.setCurrencyCode(tokenRequest.getCurrency());
        data.setCardholderName(tokenRequest.getCardHolderName());
        applePayPaymentDataDummyInitialization(data);

        return data;
    }

    private void applePayPaymentDataDummyInitialization(ApplePayPaymentData data) {
        data.setDeviceManufacturerIdentifier(ApplePayDummyConstant.DEVICE_MANUFACTURER_IDENTIFIER);
        data.setPaymentDataType(ApplePayDummyConstant.PAYMENT_DATA_TYPE);
        data.setPaymentData(ApplePayDummyConstant.getDummyPaymentData());
    }

}
