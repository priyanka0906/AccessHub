package com.priyanka.accesshub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AccessHub API")
                        .version("1.0")
                        .description("API documentation for AccessHub project"))
                .servers(List.of(new Server().url("http://localhost:8080/access-hub").description("local"),
                                  new Server().url("https://accesshub-2sbi.onrender.com/access-hub").description("deployed")))

                // Add a global security requirement
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // Register the bearerAuth scheme
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
