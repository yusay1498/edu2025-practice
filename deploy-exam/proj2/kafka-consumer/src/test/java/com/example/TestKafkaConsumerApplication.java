package com.example;

import com.example.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestKafkaConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.from(KafkaConsumerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
