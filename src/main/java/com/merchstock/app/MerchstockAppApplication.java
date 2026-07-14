package com.merchstock.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MerchstockAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchstockAppApplication.class, args);
	}

}
