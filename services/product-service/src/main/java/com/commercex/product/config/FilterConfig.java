package com.commercex.product.config;

import com.commercex.common.logging.CorrelationIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CorrelationIdFilter>
    correlationIdFilterRegistration(
            CorrelationIdFilter correlationIdFilter) {

        FilterRegistrationBean<CorrelationIdFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(correlationIdFilter);

        registration.addUrlPatterns("/*");

        registration.setOrder(1);

        return registration;
    }
}