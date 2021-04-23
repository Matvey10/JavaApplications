package ru.rbs.oauth2.authorizationserver;

import com.nimbusds.jose.jwk.JWKSet;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.KeyPair;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

//That's how our Resource Server will communicate to Authorization server to retrieve JWKs and verify that the access token's signature
// supplied in the Authorization header request from the end-user is genuine.
//@FrameworkEndpoint
class JwkSetEndpoint {
    KeyPair keyPair;

    public JwkSetEndpoint(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

//    @GetMapping("/.well-known/jwks.json")
//    @ResponseBody
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
