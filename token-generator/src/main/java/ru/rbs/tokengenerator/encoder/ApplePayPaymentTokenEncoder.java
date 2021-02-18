package ru.rbs.tokengenerator.encoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;

import java.io.IOException;

@Service
public class ApplePayPaymentTokenEncoder {

    private final static Logger logger = LoggerFactory.getLogger(ApplePayPaymentToken.class);

    @Nullable
    public String encodeBase64(ApplePayPaymentToken applePayPaymentToken) {
        if (applePayPaymentToken == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = new String(java.util.Base64.getEncoder().encode(objectMapper.writeValueAsBytes(applePayPaymentToken)));
            logger.debug(String.format("Encoded applePay payment token: [%s]", token));
            return token;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("Apple pay payment token [%s] is't encoded", applePayPaymentToken));
            return null;
        }
    }
}
