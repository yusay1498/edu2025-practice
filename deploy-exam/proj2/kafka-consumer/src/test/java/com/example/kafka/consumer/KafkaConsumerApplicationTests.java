package com.example.kafka.consumer;

import com.example.kafka.consumer.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class KafkaConsumerApplicationTests {

	@Test
	void contextLoads() {
	}

}
