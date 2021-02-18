package ru.rbs.tokengenerator.model;

import lombok.Data;

@Data
public class SimpleCurrency {
    String currency;
    boolean isDefault;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
