package com.commercex.product.config;

import io.swagger.v3.oas.models.Components;
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
public class SwaggerConfig {

    final String securitySchemeName = "BearerAuth";

    @Bean
    public OpenAPI commerceXOpenAPI() {

        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .components(
                        new Components()

                                .addSecuritySchemes(
                                        securitySchemeName,

                                        new SecurityScheme()

                                                .type(SecurityScheme.Type.HTTP)

                                                .scheme("bearer")

                                                .bearerFormat("JWT")
                                )
                )
                .info(
                        new Info()

                                .title("CommerceX Product Service API")

                                .description("REST APIs for Product Management")

                                .version("v1.0.0")

                                .contact(
                                        new Contact()

                                                .name("CommerceX Team")

                                                .email("support@commercex.com")
                                )

                                .license(
                                        new License()

                                                .name("Apache 2.0")
                                )
                )

                .externalDocs(
                        new ExternalDocumentation()

                                .description("CommerceX Documentation")

                                .url("https://commercex.com/docs")
                );
    }

}