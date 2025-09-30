package com.example.points_handler;

import com.example.points_handler.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestPointsHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.from(PointsHandlerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
