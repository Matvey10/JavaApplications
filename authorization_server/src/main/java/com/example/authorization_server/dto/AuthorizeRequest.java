package com.example.authorization_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorizeRequest {
    @JsonProperty("consent_id")
    private String consentId;
    @JsonProperty("response_type")
    String responseType;
    @JsonProperty("client_id")
    String clientId;
    @JsonProperty("username")
    String username;
    @JsonProperty("password")
    String password;
}
