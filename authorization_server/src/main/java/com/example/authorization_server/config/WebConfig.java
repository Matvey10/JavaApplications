package com.example.authorization_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebConfig {

    @Bean
    public SecurityFilterChain configure (HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated();

        return http.build();
    }

    //без него не работало, пока оставить
//    @Bean
//    public WebSecurity configure(final WebSecurity web) {
//        web.ignoring()
//            .antMatchers("/.well-known/**");
//        return web;
//    }

    @Bean
    UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user1")
            .password("password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
