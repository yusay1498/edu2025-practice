package com.example.kafka.consumer.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {
    @Bean
    public ExecutorService executorService(HikariConfig hikariConfig) {
        int threads = hikariConfig.getMaximumPoolSize() > 20 * 2
                ? hikariConfig.getMaximumPoolSize() - 20
                : hikariConfig.getMaximumPoolSize();
        return Executors.newFixedThreadPool(threads);
    }
}
