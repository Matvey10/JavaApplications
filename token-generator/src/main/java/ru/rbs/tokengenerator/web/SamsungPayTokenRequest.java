package ru.rbs.tokengenerator.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SamsungPayTokenRequest {
    @JsonProperty("merchant_login")
    private String merchantLogin;
    @JsonProperty("public_key")
    private String publicKey;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("currency_code")
    private String currencyCode;
    @JsonProperty("utc")
    private String utc;
    @JsonProperty("token_pan")
    private String tokenPAN;
    @JsonProperty("token_pan_expiration")
    private String tokenPanExpiration;

    public String getMerchantLogin() {
        return merchantLogin;
    }

    public void setMerchantLogin(String merchantLogin) {
        this.merchantLogin = merchantLogin;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public String getTokenPAN() {
        return tokenPAN;
    }

    public void setTokenPAN(String tokenPAN) {
        this.tokenPAN = tokenPAN;
    }

    public String getTokenPanExpiration() {
        return tokenPanExpiration;
    }

    public void setTokenPanExpiration(String tokenPanExpiration) {
        this.tokenPanExpiration = tokenPanExpiration;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(currencyCode) &&
                StringUtils.isNotBlank(utc) &&
                StringUtils.isNotBlank(tokenPAN) &&
                StringUtils.isNotBlank(tokenPanExpiration) &&
                StringUtils.isNotBlank(publicKey) &&
                StringUtils.isNotBlank(merchantLogin) &&
                amount != null && amount !=0;
    }
}
