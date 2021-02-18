package ru.rbs.tokengenerator.samsungpay;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SamsungPayPaymentData {
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
}
