package ru.rbs.tokengenerator.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;



@Data
//@XmlAccessorType(XmlAccessType.FIELD)
public class TokenResponse {

    @JsonIgnore
    public final static String SUCCESS_STATUS = "SUCCESS";
    @JsonIgnore
    public final static String FAIL_STATUS = "FAIL";

    private String paymentToken;
    private String status;
    private Error error;

    public static String getSuccessStatus() {
        return SUCCESS_STATUS;
    }

    public static String getFailStatus() {
        return FAIL_STATUS;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Data
    public static class Error {
        private String errorCode;
        private String description;
        private String message;

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
