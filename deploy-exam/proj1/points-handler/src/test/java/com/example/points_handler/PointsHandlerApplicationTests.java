package com.example.points_handler;

import com.example.points_handler.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PointsHandlerApplicationTests {

	@Test
	void contextLoads() {
	}

}
