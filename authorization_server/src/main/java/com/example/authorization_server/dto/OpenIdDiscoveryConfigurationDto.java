package com.example.authorization_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenIdDiscoveryConfigurationDto extends ProviderSettings {
    @JsonProperty("issuer")
    private String issuer;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    private String tokenEndpoint;
    @JsonProperty("userinfo_endpoint")
    private String userInfoEndpoint;
    @JsonProperty("registration_endpoint")
    private String registrationEndpoint;
    @JsonProperty("jwks_uri")
    private String jwksUri;
    @JsonProperty("grant_types_supported")
    private String grantTypesSupported;
    @JsonProperty("scopes_supported")
    private String scopesSupported;
    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported;
    @JsonProperty("claims_supported")
    private String claimsSupported;
    @JsonProperty("service_documentation")
    private String serviceDocumentation;
}
