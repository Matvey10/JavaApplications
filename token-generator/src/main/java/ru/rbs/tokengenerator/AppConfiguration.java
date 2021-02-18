package ru.rbs.tokengenerator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.rbs.tokengenerator.config.SecureConfig;

@SpringBootApplication
@ComponentScan(value = "ru.rbs.tokengenerator")
@Import({
        SecureConfig.class
})
public class AppConfiguration {

}
