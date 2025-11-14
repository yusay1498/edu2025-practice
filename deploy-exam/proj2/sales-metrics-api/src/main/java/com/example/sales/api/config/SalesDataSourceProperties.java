package com.example.sales.api.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@ConfigurationProperties(prefix = "spring.datasource.sales")
public class SalesDataSourceProperties extends DataSourceProperties {
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .url(getUrl())
                .username(getUsername())
                .password(getPassword())
                .build();
    }
}
