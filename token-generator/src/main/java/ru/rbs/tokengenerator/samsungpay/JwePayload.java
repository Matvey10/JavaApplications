package ru.rbs.tokengenerator.samsungpay;

import lombok.Data;

@Data
public class JwePayload {
    private String header;
    private String encryptedKey;
    private String iv;
    private String cipherText;
    private String authTag;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getAuthTag() {
        return authTag;
    }

    public void setAuthTag(String authTag) {
        this.authTag = authTag;
    }

    public String serialize(){
        return new StringBuilder(header).append(".").append(encryptedKey).append(".")
                .append(iv).append(".").append(cipherText).append(".").append(authTag).toString();
    }
}
