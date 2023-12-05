package com.mercadolibro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfig {
    @Value("${app.mercadopago.access-token}")
    private String mercadoPagoAccessToken;
    @Bean
    public String mercadoPagoAccessToken() {
        return mercadoPagoAccessToken;
    }
}
