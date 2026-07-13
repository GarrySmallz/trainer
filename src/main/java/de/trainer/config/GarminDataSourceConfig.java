package de.trainer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ConditionalOnExpression("!'${garmin.activities.datasource.url:}'.trim().isEmpty()")
public class GarminDataSourceConfig {

    @Value("${garmin.activities.datasource.url}")
    private String garminUrl;

    @Bean(name = "garminDataSource")
    public DataSource garminDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(garminUrl);
        ds.setDriverClassName("org.sqlite.JDBC");
        return ds;
    }

    @Bean(name = "garminJdbcTemplate")
    public JdbcTemplate garminJdbcTemplate(@Qualifier("garminDataSource") DataSource garminDataSource) {
        return new JdbcTemplate(garminDataSource);
    }
}