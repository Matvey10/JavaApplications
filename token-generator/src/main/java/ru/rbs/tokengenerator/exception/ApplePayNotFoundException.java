package ru.rbs.tokengenerator.exception;

import lombok.Getter;

public class ApplePayNotFoundException extends Exception{
    public static String getDescription() {
        return description;
    }

   // @Getter

    private final static String description = "The merchant does't have a certificate for encryption";

    public ApplePayNotFoundException() {
        super("Suitable ApplePay not found");
    }
}
