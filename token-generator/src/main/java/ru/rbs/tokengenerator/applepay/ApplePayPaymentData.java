package ru.rbs.tokengenerator.applepay;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class ApplePayPaymentData {

    private String applicationPrimaryAccountNumber;
    private String applicationExpirationDate;
    private String currencyCode;
    private String transactionAmount;
    private String cardholderName;
    private String deviceManufacturerIdentifier;
    private String paymentDataType;
    private PaymentData paymentData;

    public String getApplicationPrimaryAccountNumber() {
        return applicationPrimaryAccountNumber;
    }

    public void setApplicationPrimaryAccountNumber(String applicationPrimaryAccountNumber) {
        this.applicationPrimaryAccountNumber = applicationPrimaryAccountNumber;
    }

    public String getApplicationExpirationDate() {
        return applicationExpirationDate;
    }

    public void setApplicationExpirationDate(String applicationExpirationDate) {
        this.applicationExpirationDate = applicationExpirationDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getDeviceManufacturerIdentifier() {
        return deviceManufacturerIdentifier;
    }

    public void setDeviceManufacturerIdentifier(String deviceManufacturerIdentifier) {
        this.deviceManufacturerIdentifier = deviceManufacturerIdentifier;
    }

    public String getPaymentDataType() {
        return paymentDataType;
    }

    public void setPaymentDataType(String paymentDataType) {
        this.paymentDataType = paymentDataType;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(PaymentData paymentData) {
        this.paymentData = paymentData;
    }

    @Data
    public static class PaymentData {
        private String onlinePaymentCryptogram;
        private String eciIndicator;

        public String getEciIndicator() {
            return eciIndicator;
        }

        public void setEciIndicator(String eciIndicator) {
            this.eciIndicator = eciIndicator;
        }

        public String getOnlinePaymentCryptogram() {
            return onlinePaymentCryptogram;
        }

        public void setOnlinePaymentCryptogram(String onlinePaymentCryptogram) {
            this.onlinePaymentCryptogram = onlinePaymentCryptogram;
        }
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(applicationPrimaryAccountNumber) && StringUtils.isNotBlank(applicationExpirationDate)
                && StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(transactionAmount) && paymentData != null;
    }
}
