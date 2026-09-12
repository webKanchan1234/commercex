package com.commercex.auth.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI commerceXOpenAPI() {

        final String securityScheme = "Bearer Authentication";

        return new OpenAPI()

                .info(

                        new Info()

                                .title("CommerceX Auth Service API")

                                .version("v1.0.0")

                                .description("""
                                        Production-grade Authentication Microservice.
                                        
                                        Features:
                                        • JWT Authentication
                                        • Refresh Token Rotation
                                        • Multi-Device Session Management
                                        • Email Verification
                                        • Forgot Password
                                        • Reset Password
                                        • RBAC Authorization
                                        """)

                                .contact(
                                        new Contact()
                                                .name("Kanchan Kumar")
                                                .email("your-email@example.com")
                                )

                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securityScheme)
                )

                .schemaRequirement(
                        securityScheme,
                        new SecurityScheme()
                                .name(securityScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description("CommerceX Documentation")
                                .url("https://github.com/webKanchan1234")
                );
    }
}