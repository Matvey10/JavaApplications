package ru.rbs.tokengenerator.applepay;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplePayPaymentToken implements Serializable{

    private String version;
    private String signature;
    private Header header;
    private String data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Data
    public static class Header implements Serializable{
        private String wrappedKey;
        private String ephemeralPublicKey;
        private String publicKeyHash;
        private String transactionId;

        public String getWrappedKey() {
            return wrappedKey;
        }

        public void setWrappedKey(String wrappedKey) {
            this.wrappedKey = wrappedKey;
        }

        public String getEphemeralPublicKey() {
            return ephemeralPublicKey;
        }

        public void setEphemeralPublicKey(String ephemeralPublicKey) {
            this.ephemeralPublicKey = ephemeralPublicKey;
        }

        public String getPublicKeyHash() {
            return publicKeyHash;
        }

        public void setPublicKeyHash(String publicKeyHash) {
            this.publicKeyHash = publicKeyHash;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Header{");
            sb.append("wrappedKey='").append(wrappedKey != null ? "****" : null).append('\'');
            sb.append(", ephemeralPublicKey='").append(ephemeralPublicKey != null ? "****" : null).append('\'');
            sb.append(", publicKeyHash='").append(publicKeyHash).append('\'');
            sb.append(", transactionId='").append(transactionId).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApplePayPaymentToken{");
        sb.append("version='").append(version).append('\'');
        sb.append(", signature='").append(signature != null ? "****" : null).append('\'');
        sb.append(", header=").append(header);
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
