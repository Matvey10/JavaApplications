package ru.rbs.tokengenerator.model;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class SimpleMerchant {
    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantLogin() {
        return merchantLogin;
    }

    public void setMerchantLogin(String merchantLogin) {
        this.merchantLogin = merchantLogin;
    }

    @Nullable
    public SimpleCurrency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(@Nullable SimpleCurrency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    private long merchantId;
    private String merchantLogin;
    @Nullable
    private SimpleCurrency defaultCurrency;
}
