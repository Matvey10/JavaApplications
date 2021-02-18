package ru.rbs.tokengenerator.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Value;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import ru.rbs.tokengenerator.exception.ApplePayNotFoundException;
import ru.rbs.tokengenerator.exception.ApplePayPaymentDataValidationException;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.exception.RequestParamException;
import ru.rbs.tokengenerator.service.EncodingService;
import ru.rbs.tokengenerator.web.GooglePayTokenRequest;
import ru.rbs.tokengenerator.web.TokenRequest;
import ru.rbs.tokengenerator.web.TokenResponse;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class TokenGeneratorController {
    private final static Logger logger = LoggerFactory.getLogger(TokenGeneratorController.class);

    @Resource
    private EncodingService encodingService;

    @ExceptionHandler(Exception.class)
    public TokenResponse exceptionHandler(Exception e) {
        logger.error("Error: ", e);

        if (e instanceof MerchantNotFoundException) {
            return createErrorResponse("1", MerchantNotFoundException.getDescription(), e.getMessage());
        }
        if (e instanceof ApplePayNotFoundException) {
            return createErrorResponse("2", ApplePayNotFoundException.getDescription(), e.getMessage());
        }
        if (e instanceof HttpMessageNotReadableException && StringUtils.contains(e.getMessage(), "Unrecognized field \"")) {
            return createErrorResponse("3", "Request format error", "Unknown parameter : '" + StringUtils.substringBetween(e.getMessage(), "Unrecognized field \"", "\"") + "'");
        }
        if (e instanceof HttpMessageNotReadableException && StringUtils.contains(e.getMessage(), "Unexpected character")) {
            return createErrorResponse("3", "Request format error", "Unexpected character : '" + StringUtils.substringBetween(e.getMessage(), "Unexpected character ('", "'") + "'");
        }
        if (e instanceof RequestParamException) {
            return createErrorResponse("4", RequestParamException.getDescription(), e.getMessage());
        }

        return createErrorResponse("5", "System error", "Payment token wasn't generated: " + e.getMessage());
    }

    @RequestMapping(value = "/rest/generateApplePayPaymentToken.do")
    public TokenResponse getToken(@ModelAttribute TokenRequest tokenRequest) throws RequestParamException, ApplePayPaymentDataValidationException, MerchantNotFoundException, ApplePayNotFoundException {
        logger.debug("Incoming request object {}", tokenRequest);

        tokenRequest.validate();
        return createSuccessfulResponse(encodingService.encodingRequestDataToPaymentToken(tokenRequest));
    }


    @RequestMapping(value = "/rest/generateGooglePayPaymentToken.do")
    public String getGooglePayToken(@RequestParam String tokenRequestJson, @RequestParam String payload) throws RequestParamException, IOException {
        logger.debug("Incoming request: {}, payload: {}", tokenRequestJson, payload);
        GooglePayTokenRequest tokenRequest = new ObjectMapper().readValue(tokenRequestJson, GooglePayTokenRequest.class);
        tokenRequest.validate();
        String token = encodingService.generateGooglePayToken(tokenRequest, payload);
        return Base64.encodeBase64String(token.getBytes());
    }

    private TokenResponse createErrorResponse(String errorCode, String description, String errorMessage) {
        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setPaymentToken(null);
        tokenResponse.setStatus(TokenResponse.FAIL_STATUS);
        tokenResponse.setError(new TokenResponse.Error());
        tokenResponse.getError().setDescription(description);
        tokenResponse.getError().setMessage(errorMessage);
        tokenResponse.getError().setErrorCode(errorCode);

        logger.debug("Created error response [{}]", tokenResponse);
        return tokenResponse;
    }

    private TokenResponse createSuccessfulResponse(String token) {
        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setPaymentToken(token);
        tokenResponse.setStatus(TokenResponse.SUCCESS_STATUS);
        tokenResponse.setError(new TokenResponse.Error());
        tokenResponse.getError().setDescription("token was successfully generated");
        tokenResponse.getError().setMessage("success");
        tokenResponse.getError().setErrorCode("0");

        logger.debug("Created successful response [{}]", tokenResponse);
        return tokenResponse;
    }
}
