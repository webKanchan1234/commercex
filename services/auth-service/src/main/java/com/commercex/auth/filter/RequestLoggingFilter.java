package com.commercex.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString();

        // Store Trace ID in MDC for logging
        MDC.put("traceId", traceId);

        // Return Trace ID to the client
        response.setHeader("X-Trace-Id", traceId);

        long start = System.currentTimeMillis();

        try {

            log.info(
                    "Incoming Request {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);

        } finally {

            long executionTime =
                    System.currentTimeMillis() - start;

            log.info(
                    "Completed {} {} Status={} Time={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    executionTime
            );

            MDC.clear();
        }
    }

}