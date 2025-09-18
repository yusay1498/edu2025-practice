package com.example.kafka_message_handler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class KafkaMessageHandlerApplicationTests {

	@Test
	void contextLoads() {
	}

}
