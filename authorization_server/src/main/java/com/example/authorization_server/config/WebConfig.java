package com.example.authorization_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebConfig {
    @Bean
    public SecurityFilterChain configure (HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .authorizeRequests()
            .anyRequest().authenticated();
        return http.build();
    }

    //без него не работало, пока оставить
//    @Bean
//    public WebSecurity configure(final WebSecurity web) {
//        web.ignoring()
//            .antMatchers("/code");
//        return web;
//    }
}
