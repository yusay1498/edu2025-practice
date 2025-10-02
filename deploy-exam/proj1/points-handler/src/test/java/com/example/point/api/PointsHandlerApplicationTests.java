package com.example.point.api;

import com.example.point.api.config.TestcontainersConfiguration;
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
