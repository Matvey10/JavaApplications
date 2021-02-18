package ru.rbs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rbs.decoders.apple.RsaApplePayDecoder;
import ru.rbs.tokengenerator.service.EncodingService;

@Configuration
public class EncodingTestConfig {
    @Bean
    public EncodingService getEncodingService(){
        return new EncodingService();
    }
    @Bean
    public RsaApplePayDecoder getDecoder(){
        return new RsaApplePayDecoder();
    }
}
