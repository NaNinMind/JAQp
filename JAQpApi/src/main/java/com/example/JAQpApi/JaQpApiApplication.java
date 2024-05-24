package com.example.JAQpApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class JaQpApiApplication
{
	private static final Logger logger = LoggerFactory.getLogger(JaQpApiApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JaQpApiApplication.class, args);
	}
}
