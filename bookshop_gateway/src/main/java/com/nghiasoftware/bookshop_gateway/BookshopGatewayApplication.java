package com.nghiasoftware.bookshop_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookshopGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookshopGatewayApplication.class, args);
	}

}
