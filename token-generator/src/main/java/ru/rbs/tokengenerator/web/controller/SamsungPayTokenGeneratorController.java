package ru.rbs.tokengenerator.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.rbs.tokengenerator.service.SamsungPayEncodingService;
import ru.rbs.tokengenerator.web.SamsungPayTokenRequest;

@RestController
public class SamsungPayTokenGeneratorController {
    @Autowired
    SamsungPayEncodingService encodingService;

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/rest/generateSamsungPayPaymentToken.do")
    public String generateSamsungPayPaymentToken(@RequestBody SamsungPayTokenRequest tokenRequest) throws Exception {
        System.out.println(tokenRequest.toString());
        return encodingService.encodeRequestDataToSamsungPayPaymentToken(tokenRequest);
    }
}
