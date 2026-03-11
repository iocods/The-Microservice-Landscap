package com.iocodes.app.microservice.core.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.iocodes.app"})
public class ReviewServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceApplication.class);
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ReviewServiceApplication.class, args);

		String postgresDbUrl = ctx.getEnvironment().getProperty("spring.datasource.url");

		LOG.info("Connected to Postgresql: {}", postgresDbUrl);
	}

}
