package com.example.authorization_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthenticationController {
    @GetMapping("auth")
    public String authoriseUser() {
        return "SUCCESS";
    }
}
