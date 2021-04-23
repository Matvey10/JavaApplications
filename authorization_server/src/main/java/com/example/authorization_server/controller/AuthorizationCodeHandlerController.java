package com.example.authorization_server.controller;

import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationCodeHandlerController {
    @GetMapping("code")
    public String getAuthorizationCode(@RequestParam("code") String code) {
        JSONObject response = new JSONObject();
        response.put("code", code);
        return response.toJSONString();
    }
}
