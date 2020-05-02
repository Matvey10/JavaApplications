package com.company.config;

import com.company.Application;
import com.company.controllers.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
    @Bean
    public Logger getLogger(){
        Logger logger = LoggerFactory.getLogger(Application.class);
        return logger;
    }
}
