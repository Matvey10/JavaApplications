package ru.rbs.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

@Configuration
public class ClientDetailsConfig {
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        addTestClientDetails(clientDetailsService);
        return clientDetailsService;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void addTestClientDetails(JdbcClientDetailsService clientDetailsService) {
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("test-client");
        clientDetails.setClientSecret("secret");
        clientDetailsService.addClientDetails(clientDetails);
    }
}
