package de.trainer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class GarminMonitoringDataSourceConfig {

    @Value("${garmin.monitoring.datasource.url}")
    private String url;

    @Bean(name = "garminMonitoringDataSource")
    public DataSource garminMonitoringDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setDriverClassName("org.sqlite.JDBC");
        return ds;
    }

    @Bean(name = "garminMonitoringJdbcTemplate")
    public JdbcTemplate garminMonitoringJdbcTemplate(@Qualifier("garminMonitoringDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
