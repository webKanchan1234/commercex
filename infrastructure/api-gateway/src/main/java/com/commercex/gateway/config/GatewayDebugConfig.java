package com.commercex.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayDebugConfig {

    @Bean
    public GlobalFilter debugFilter() {

        return (exchange, chain) -> {

            System.out.println(
                    "REQUEST: "
                            + exchange.getRequest().getMethod()
                            + " "
                            + exchange.getRequest().getURI()
                            + " HOST="
                            + exchange.getRequest()
                            .getHeaders()
                            .getFirst("Host")
                            + " USER_AGENT="
                            + exchange.getRequest()
                            .getHeaders()
                            .getFirst("User-Agent")
            );

            return chain.filter(exchange);
        };
    }
}