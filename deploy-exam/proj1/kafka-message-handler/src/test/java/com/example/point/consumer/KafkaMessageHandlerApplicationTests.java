package com.example.point.consumer;

import com.example.point.consumer.config.TestcontainersConfiguration;
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
