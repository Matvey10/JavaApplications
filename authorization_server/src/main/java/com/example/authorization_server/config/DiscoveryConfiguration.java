package com.example.authorization_server.config;

import com.example.authorization_server.dto.OpenIdDiscoveryConfigurationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import java.util.List;

@Configuration
public class DiscoveryConfiguration {
    @Value("${oauth.endpoints.authorization}")
    private String authorizationEndpoint;
    @Value("${oauth.endpoints.registration}")
    private String registrationEndpoint;
    @Value("${oauth.endpoints.token}")
    private String tokenEndpoint;
    @Value("${oauth.endpoints.issuer}")
    private String issuer;
    @Value("${oauth.endpoints.jwks}")
    private String jwksUri;

    @Bean
    public ProviderSettings getProviderSettings() {
        return new ProviderSettings().issuer(issuer)
            .authorizationEndpoint(authorizationEndpoint)
            .tokenEndpoint(tokenEndpoint)
            .jwkSetEndpoint(jwksUri);
            //заменить на кастомные - запихнуть в детали и передать
    }
}
