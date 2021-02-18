package ru.rbs.decoders.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import ru.bpc.phoenix.model.ApplePay;
import ru.bpc.service.CertificateService;
import ru.rbs.commons.encrypt.CryptoService;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;
import ru.rbs.tokengenerator.dao.ApplePayDao;
import ru.rbs.tokengenerator.dao.MerchantDao;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.model.SimpleMerchant;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;

public abstract class AbstractApplePayDecoder implements ApplePayDecoder {

    private final static Logger logger = LoggerFactory.getLogger(AbstractApplePayDecoder.class);

    @Autowired
    private MerchantDao merchantDao;
    @Resource
    private ApplePayDao applePayDao;
    @Resource
    private CryptoService aesCryptUtil;
    @Resource
    private ObjectMapper objectMapper;

    public ApplePayPaymentData decodeToken(@NotNull String paymentToken, @NotNull String merchantLogin) throws MerchantNotFoundException {
        ApplePayPaymentToken token = decrypt(paymentToken);
        SimpleMerchant simpleMerchant = merchantDao.findByMerchantLogin(merchantLogin);
        List<ApplePay> applePays = applePayDao.findByMerchantId(simpleMerchant.getMerchantId());
        ApplePayPaymentData applePayPaymentData = decode(applePays, token);
        return applePayPaymentData;
    }

    public ApplePayPaymentToken decrypt(@NotNull String paymentToken) {
        Assert.notNull(paymentToken, "paymentToken must be validated by the caller");
        ApplePayPaymentToken applePayPaymentToken;
        try {
            applePayPaymentToken = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(paymentToken)), ApplePayPaymentToken.class);
            return applePayPaymentToken;
        } catch (IOException e) {
            logger.error("Failed to decode token: {}", paymentToken, e);
            return null;
        }
    }

    @Nullable
    @Override
    public ApplePayPaymentData decode(@NotNull List<ApplePay> applePays, @NotNull ApplePayPaymentToken applePayPaymentToken) {
        ApplePayPaymentData applePayPaymentData = null;
        for (ApplePay applePay : applePays) {
            try {
                if (!isValidate(applePay)) {
                    continue;
                }
                String privateKey = applePay.getPrivateKey();
                if (StringUtils.isBlank(privateKey)) {
                    continue;
                }
                if (StringUtils.isBlank(applePay.getPublicKeyHash())) {
                    String publicKeyHash = CertificateService.getPublicKeyHash(applePay.getPublicKey());
                    logger.debug("{} does not have publicKeyHash, try to generate and save as: {}", applePay, publicKeyHash);
                    applePay.setPublicKeyHash(publicKeyHash);
                    applePayDao.update(applePay);
                }
                if (!applePayPaymentToken.getHeader().getPublicKeyHash().equals(applePay.getPublicKeyHash())) {
                    logger.debug("publicKeyHashes do not match, {} , go to next.", applePay);
                    continue;
                }
                PrivateKey pk = CertificateService.getPrivateKeyFromString(new String(aesCryptUtil.decrypt(Base64.getDecoder().decode(privateKey))), getCertificateType().getAlgorithm());
                String decString = decode(pk, applePay, applePayPaymentToken);
                logger.debug("data='" + decString + "'");
                applePayPaymentData = objectMapper.readValue(decString, ApplePayPaymentData.class);
                if (applePayPaymentData.isValid()) {
                    if (applePay.getId() == null) {
                        logger.debug("Apple pay payment data decoded with mass key pair");
                    } else {
                        logger.debug("Apple pay payment data decoded with own or parent key pair");
                    }
                    logger.debug("Payment data decoded");
                    return applePayPaymentData;
                }
            } catch (Exception e) {
                logger.error("Failed to decode token: {}", applePayPaymentToken, e);
            }
        }
        return null;
    }

    protected boolean isValidate(ApplePay applePay) {
        return true;
    }

    protected abstract String decode(PrivateKey pk, ApplePay applePay, ApplePayPaymentToken applePayPaymentToken) throws Exception;

}

