package com.commercex.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestLoggingFilter
        implements org.springframework.cloud.gateway.filter.GlobalFilter,
        Ordered {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        long startTime =
                System.currentTimeMillis();

        String method =
                exchange.getRequest()
                        .getMethod()
                        .name();

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        String correlationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(
                                CorrelationIdFilter.CORRELATION_ID
                        );

        log.info(
                "Request started | method={} path={} correlationId={}",
                method,
                path,
                correlationId
        );

        return chain.filter(exchange)
                .doFinally(signal -> {

                    long duration =
                            System.currentTimeMillis()
                                    - startTime;

                    int status =
                            exchange.getResponse()
                                    .getStatusCode() != null
                                    ? exchange.getResponse()
                                    .getStatusCode()
                                    .value()
                                    : 0;

                    log.info(
                            "Request completed | method={} path={} status={} duration={}ms correlationId={}",
                            method,
                            path,
                            status,
                            duration,
                            correlationId
                    );
                });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}