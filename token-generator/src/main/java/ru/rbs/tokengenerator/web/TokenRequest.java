package ru.rbs.tokengenerator.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.exception.RequestParamException;



@Data
//@XmlAccessorType(XmlAccessType.FIELD)
public class TokenRequest {

    private String pan;
    private String amount;
    private String currency;
    private String cardHolderName;
    private String expiry;
    private String algorithm;
    private String merchantLogin;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMerchantLogin() {
        return merchantLogin;
    }

    public void setMerchantLogin(String merchantLogin) {
        this.merchantLogin = merchantLogin;
    }

    @JsonIgnore
    public void validate() throws RequestParamException {

        if (StringUtils.isBlank(pan)) {
            throw new RequestParamException("pan", "missing");
        }

        if (StringUtils.isBlank(amount)) {
            throw new RequestParamException("amount", "missing");
        }

        if (StringUtils.isBlank(expiry)) {
            throw new RequestParamException("expiry", "missing");
        }

        if (!EnumUtils.isValidEnum(ApplePayCertificateAlgorithm.class, algorithm)) {
            throw new RequestParamException("algorithm", "not supported");
        }

        if (StringUtils.isBlank(merchantLogin)) {
            throw new RequestParamException("merchantLogin", "missing");
        }

    }
}
