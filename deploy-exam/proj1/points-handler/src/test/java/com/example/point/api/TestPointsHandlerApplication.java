package com.example.point.api;

import com.example.point.api.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestPointsHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.from(PointsHandlerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
