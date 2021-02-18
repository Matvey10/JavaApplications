package ru.rbs.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import java.security.KeyPair;

public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Resource
    private ClientDetailsService clientDetailsService;

    AuthenticationManager authenticationManager;
    KeyPair keyPair;
    PasswordEncoder passwordEncoder;
    UserDetailsService userDetailsService;
    private String clientid = "tutorialspoint";
    private String clientSecret = "my-secret-key";
    private String privateKey = "private key";
    private String publicKey = "public key";

    public AuthServerConfig (AuthenticationConfiguration authenticationConfiguration, KeyPair keyPair, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.keyPair = keyPair;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    //настройка для клиентов
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(clientid)
            .secret(clientSecret)
            .scopes("read", "write")
            .authorizedGrantTypes("password", "refresh_token")
            .accessTokenValiditySeconds(20000)
            .refreshTokenValiditySeconds(20000);
//        clients.withClientDetails(clientDetailsService); //когда добавлю бд
    }

    //используется для encode/decode токенов - нужно тут настроить и в ресурс-сервере
    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(privateKey);
//        converter.setVerifierKey(publicKey); // пока симметрично делаем
        return converter;
    }

    //используется для хранения токенов
    @Bean
    public JwtTokenStore tokenStore(JwtAccessTokenConverter tokenEnhancer) {
        return new JwtTokenStore(tokenEnhancer);
    }

    /*Базовая реализация для служб токенов с использованием случайных значений UUID для токена доступа и значений токена обновления.
    Основной точкой расширения для настроек является TokenEnhancer, который будет вызываться после создания токенов доступа и обновления,
    но до их сохранения. Сохранение делегируется реализации TokenStore, а настройка токена доступа - TokenEnhancer. */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices(JwtTokenStore jwtTokenStore) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    //везде почти одинаково - как тут, зачем это хз
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .allowFormAuthenticationForClients(); // (1)Replaces Basic Authentication and allows you to pass all necessary params as a part of a request body
    }
}
