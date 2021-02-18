package ru.rbs.tokengenerator.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import ru.rbs.tokengenerator.exception.RequestParamException;

@Data
@JsonSerialize
public class GooglePayTokenRequest {
    private String encryptionPublicKey;
    private String signingPrivateKey;
    private String recipientId;

    public String getEncryptionPublicKey() {
        return encryptionPublicKey;
    }

    public void setEncryptionPublicKey(String encryptionPublicKey) {
        this.encryptionPublicKey = encryptionPublicKey;
    }

    public String getSigningPrivateKey() {
        return signingPrivateKey;
    }

    public void setSigningPrivateKey(String signingPrivateKey) {
        this.signingPrivateKey = signingPrivateKey;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    @JsonIgnore
    public void validate() throws RequestParamException {

        if (StringUtils.isBlank(encryptionPublicKey)) {
            throw new RequestParamException("encryptionPublicKey", "missing");
        }

        if (StringUtils.isBlank(signingPrivateKey)) {
            throw new RequestParamException("signingPrivateKey", "missing");
        }

        if (StringUtils.isBlank(recipientId)) {
            throw new RequestParamException("recipientId", "missing");
        }

    }


}
