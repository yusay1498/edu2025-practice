package com.example.kafka_message_handler;

import com.example.kafka_message_handler.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestKafkaMessageHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.from(KafkaMessageHandlerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
