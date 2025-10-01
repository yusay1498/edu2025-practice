package com.example;

import com.example.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestKafkaMessageHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.from(KafkaMessageHandlerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
