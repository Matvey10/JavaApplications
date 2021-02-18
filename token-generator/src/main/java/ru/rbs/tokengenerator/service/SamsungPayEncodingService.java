package ru.rbs.tokengenerator.service;

import org.springframework.stereotype.Service;
import ru.bpc.phoenix.model.SamsungPay;
import ru.rbs.tokengenerator.dao.MerchantDao;
import ru.rbs.tokengenerator.dao.SamsungPayDao;
import ru.rbs.tokengenerator.model.SimpleMerchant;
import ru.rbs.tokengenerator.samsungpay.SamsungPayDummyConstant;
import ru.rbs.tokengenerator.samsungpay.SamsungPayEncoder;
import ru.rbs.tokengenerator.samsungpay.SamsungPayPaymentToken;
import ru.rbs.tokengenerator.web.SamsungPayTokenRequest;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SamsungPayEncodingService {
    @Resource
    private MerchantDao merchantDao;
    @Resource
    private SamsungPayDao samsungPayDao;

    public String encodeRequestDataToSamsungPayPaymentToken (SamsungPayTokenRequest tokenRequest) throws Exception {
        if (!tokenRequest.isValid())
            throw new Exception("bad token request");
        SimpleMerchant merchant = merchantDao.findByMerchantLogin(tokenRequest.getMerchantLogin());
        List<SamsungPay> samsungPays = samsungPayDao.findByMerchantId(merchant.getMerchantId());
        String encodingPaymentToken = encode(samsungPays, tokenRequest);
        return encodingPaymentToken;
    }

    private String encode(List<SamsungPay> samsungPays, SamsungPayTokenRequest tokenRequest) throws Exception {
        SamsungPayPaymentToken paymentToken = convertToSamsungPayPaymentToken(tokenRequest);
        for (SamsungPay samsungPay : samsungPays){
            if (!samsungPay.getPublicKey().equals(tokenRequest.getPublicKey())){
                continue;
            }
            return SamsungPayEncoder.encode(samsungPay.getPublicKey(), paymentToken);
        }
        throw new Exception("no valid samsung pays");
    }

    private SamsungPayPaymentToken convertToSamsungPayPaymentToken(SamsungPayTokenRequest tokenRequest){
        SamsungPayPaymentToken paymentToken = new SamsungPayPaymentToken();
        paymentToken.setAmount(tokenRequest.getAmount());
        paymentToken.setCurrencyCode(tokenRequest.getCurrencyCode());
        paymentToken.setUtc(tokenRequest.getUtc());
        paymentToken.setTokenPAN(tokenRequest.getTokenPAN());
        paymentToken.setTokenPanExpiration(tokenRequest.getTokenPanExpiration());
        paymentToken.setEciIndicator(SamsungPayDummyConstant.ECI_INDICATOR);
        paymentToken.setCryptogram(SamsungPayDummyConstant.CRYPTOGRAM);
        return paymentToken;
    }
}
