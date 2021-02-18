package ru.rbs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.rbs.tokengenerator.config.SecureConfig;
import ru.rbs.tokengenerator.service.EncodingService;

@SpringBootApplication
@ComponentScan(value = "ru.rbs.tokengenerator")
@Import({
        SecureConfig.class,
        TestDatasourceConfig.class,
        EncodingTestConfig.class
})
public class TestMainConfiguration {
}
