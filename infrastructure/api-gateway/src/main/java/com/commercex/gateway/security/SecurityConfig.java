package com.commercex.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfig(
            JwtAuthenticationConverter jwtAuthenticationConverter) {

        this.jwtAuthenticationConverter =
                jwtAuthenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange

                        .pathMatchers(
                                "/actuator/**"
                        ).permitAll()

                        .pathMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        .pathMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        .pathMatchers(
                                "/api/v1/products/**"
                        ).hasAnyAuthority(
                                "ROLE_CUSTOMER",
                                "ROLE_ADMIN"
                        )

                        .pathMatchers(
                                "/api/v1/inventory/**"
                        ).hasAuthority(
                                "ROLE_ADMIN"
                        )

                        .pathMatchers(
                                "/api/v1/orders/**"
                        ).hasAnyAuthority(
                                "ROLE_CUSTOMER",
                                "ROLE_ADMIN"
                        )

                        .anyExchange()
                        .authenticated()
                )

                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(jwt ->
                                        jwt.jwtAuthenticationConverter(
                                                jwtAuthenticationConverter
                                        )
                                )
                )

                .build();
    }
}