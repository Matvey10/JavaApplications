package ru.rbs;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.util.Arrays;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
public abstract class TestDatasourceConfig {
    @Value("${data.scripts:}")
    private String optionalScripts;

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setName("Test DB " + Math.random())
                .setType(HSQL)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:data/test_schema.sql");

        Arrays.stream(optionalScripts.split(","))
                .filter(StringUtils::isNotBlank)
                .forEach(builder::addScript);

        return builder.build();
    }
}