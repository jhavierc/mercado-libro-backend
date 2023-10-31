package com.mercadolibro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {
    private final String controllersBasePackage = "com.mercadolibro.controller";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage(controllersBasePackage))
                .build().apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme
                        .JWT_BEARER_BUILDER
                        .name("JWT Token")
                        .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MercadoLibro")
                .description("API for MercadoLibro")
                .version("1.0")
                .build();
    }
}
