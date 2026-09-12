package com.commercex.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.commercex")
//@EnableCaching
public class ProductServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                ProductServiceApplication.class,
                args
        );

        log.info("========================================");
        log.info(" Product Service Started Successfully");
        log.info("========================================");


    }

}