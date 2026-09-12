package com.commercex.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.commercex")
@EnableDiscoveryClient
//@EnableFeignClients
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);

        log.info("========================================");
        log.info(" Inventory Service Started Successfully");
        log.info("========================================");
	}

}
