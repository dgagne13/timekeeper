package net.chronos.timekeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@SpringBootApplication
public class TimeKeeperApplication {
    public static void main(String...args) {
        SpringApplication.run(TimeKeeperApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.timekeeper")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }


}
