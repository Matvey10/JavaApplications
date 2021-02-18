package ru.rbs.tokengenerator.samsungpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SamsungPayPaymentToken {
    @JsonProperty("amount")
    private long amount;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("utc")
    private String utc;

    @JsonProperty("eci_indicator")
    private String eciIndicator;

    @JsonProperty("tokenPAN")
    private String tokenPAN;

    @JsonProperty("tokenPanExpiration")
    private String tokenPanExpiration;

    @JsonProperty("cryptogram")
    private String cryptogram;

    public boolean areMandatoryFieldsNotBlank() {
        return StringUtils.isNotBlank(currencyCode) &&
                StringUtils.isNotBlank(utc) &&
                StringUtils.isNotBlank(eciIndicator) &&
                StringUtils.isNotBlank(tokenPAN) &&
                StringUtils.isNotBlank(tokenPanExpiration) &&
                StringUtils.isNotBlank(cryptogram);
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
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

    public String getEciIndicator() {
        return eciIndicator;
    }

    public void setEciIndicator(String eciIndicator) {
        this.eciIndicator = eciIndicator;
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

    public String getCryptogram() {
        return cryptogram;
    }

    public void setCryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
    }

    @Override
    public String toString() {
        return "SamsungPayPaymentToken{" +
                "amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                ", utc='" + utc + '\'' +
                ", eciIndicator='" + eciIndicator + '\'' +
                ", tokenPAN='" + tokenPAN + '\'' +
                ", tokenPanExpiration='" + tokenPanExpiration + '\'' +
                ", cryptogram='" + cryptogram + '\'' +
                '}';
    }
}

