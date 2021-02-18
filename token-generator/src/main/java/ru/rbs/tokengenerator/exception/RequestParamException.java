package ru.rbs.tokengenerator.exception;

import lombok.Getter;

public class RequestParamException extends Exception {

    public static String getDescription() {
        return description;
    }

    @Getter

    private final static String description = "Request param error";

    public RequestParamException(String param, String cause) {
        super(String.format("Parameter [%s] is %s", param, cause));
    }

    public RequestParamException(String message) {
        super(message);
    }
}
