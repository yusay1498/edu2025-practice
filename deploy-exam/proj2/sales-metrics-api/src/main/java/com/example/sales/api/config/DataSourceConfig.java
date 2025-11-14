package com.example.sales.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({SalesDataSourceProperties.class, CustomerItemDataSourceProperties.class})
public class DataSourceConfig {

    // Sales用のDataSource
    @Bean
    public DataSource salesDataSource(SalesDataSourceProperties salesProperties) {
        return new DriverManagerDataSource(
                salesProperties.getUrl(),
                salesProperties.getUsername(),
                salesProperties.getPassword()
        );
    }

    // CustomerItem用のDataSource
    @Bean
    public DataSource customerItemDataSource(CustomerItemDataSourceProperties customerItemProperties) {
        return new DriverManagerDataSource(
                customerItemProperties.getUrl(),
                customerItemProperties.getUsername(),
                customerItemProperties.getPassword()
        );
    }

    // Sales用のJdbcClient
    @Bean
    @SalesJdbcClient
    public JdbcClient salesJdbcClient(DataSource salesDataSource) {
        return JdbcClient.create(salesDataSource);
    }

    // CustomerItem用のJdbcClient
    @Bean
    @CustomerItemJdbcClient
    public JdbcClient customerItemJdbcClient(DataSource customerItemDataSource) {
        return JdbcClient.create(customerItemDataSource);
    }
}
