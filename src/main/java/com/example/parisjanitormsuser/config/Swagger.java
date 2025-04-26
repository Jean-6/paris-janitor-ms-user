package com.example.parisjanitormsuser.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info().description("API Documentation")
                        .version("1.0.0")
                        .description("Documentation d'API REST - Microservices utilisateurs")
                        .contact(new Contact()
                                        .name("Jaures Support Dev")
                                        .email("oka.jeanjaures@gmail.com")
                                        .url("https://github.com/Jean-6/paris-janitor-spring-boot")
                        )
                );
    }

    @Bean
    public GroupedOpenApi apiGroup(){
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan("com.example.parisjanitormsuser")
                .build();
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return OpenApi -> OpenApi.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperations().forEach(operation -> {
                operation.addTagsItem("Custom Tag");
            });
        });
    }





}
