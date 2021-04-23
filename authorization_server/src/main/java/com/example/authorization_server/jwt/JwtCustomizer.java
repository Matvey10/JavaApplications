package com.example.authorization_server.jwt;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.stereotype.Service;

import static org.springframework.security.oauth2.core.OAuth2TokenType.ACCESS_TOKEN;

@Service
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    private String AUTHORIZATION_REQUEST_NAME = "org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest";

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType() == ACCESS_TOKEN) {
            OAuth2AuthorizationRequest authorizationRequest = context.getAuthorization().getAttribute(AUTHORIZATION_REQUEST_NAME);
            String consentId = (String) authorizationRequest.getAdditionalParameters().get("consent_id");
            context.getClaims().claim("consent_id", consentId);
        }
    }
}
