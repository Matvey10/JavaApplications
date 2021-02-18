package ru.rbs.tokengenerator.exception;

import lombok.Getter;

public class MerchantNotFoundException extends Exception{

    public static String getDescription() {
        return description;
    }

    @Getter

    private final static String description = "Merchant not found";

    public MerchantNotFoundException(String message) {
        super(message);
    }
}
