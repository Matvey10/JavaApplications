package com.example.authorization_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class OAuth2AuthorizationServerFilterChainConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain customAuthorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        applyDefaultSecurity(http);
        return http.build();
    }

    public static void applyDefaultSecurity(HttpSecurity http) throws Exception {
        //AuthorizationCodeHandlerFilter authorizationCodeHandlerFilter = new AuthorizationCodeHandlerFilter();
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
            new OAuth2AuthorizationServerConfigurer<>();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer
            .getEndpointsMatcher();

        http
            .requestMatcher(endpointsMatcher)
            .authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated()
            )
            //.addFilterAfter(authorizationCodeHandlerFilter, CsrfFilter.class)
            .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
            .apply(authorizationServerConfigurer);
    }
}
