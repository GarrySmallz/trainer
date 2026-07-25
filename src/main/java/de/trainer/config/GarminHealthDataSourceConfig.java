package de.trainer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class GarminHealthDataSourceConfig {

    @Value("${garmin.health.datasource.url}")
    private String url;

    @Bean(name = "garminHealthDataSource")
    public DataSource garminHealthDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setDriverClassName("org.sqlite.JDBC");
        return ds;
    }

    @Bean(name = "garminJdbcHealthTemplate")
    public JdbcTemplate garminJdbcHealthTemplate(@Qualifier("garminHealthDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
