package ru.rbs.oauth2.authorizationserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

@Configuration
class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken;
        ResourceServerTokenServices resourceServerTokenServices;
        BearerTokenAuthentication bearerTokenAuthentication;
        DefaultOAuth2AccessToken defaultOAuth2AccessToken;



        super.configure(http);
        http
            .requestMatchers()
            .mvcMatchers("/.well-known/jwks.json")
            .and()
            .authorizeRequests()
            .mvcMatchers("/.well-known/jwks.json").permitAll();
    }
}
