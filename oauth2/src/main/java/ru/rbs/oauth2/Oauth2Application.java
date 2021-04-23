package ru.rbs.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.rbs.oauth2.testauth.AuthServerConfig;
import ru.rbs.oauth2.testauth.JwtConfig;
import ru.rbs.oauth2.testauth.UserDetailsConfig;
import ru.rbs.oauth2.testauth.WebSecurityConfig;

@SpringBootApplication
@Import({
    AuthServerConfig.class,
    JwtConfig.class,
    UserDetailsConfig.class,
    WebSecurityConfig.class
})
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
