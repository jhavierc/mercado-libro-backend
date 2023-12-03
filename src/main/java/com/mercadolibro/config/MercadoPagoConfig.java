package com.mercadolibro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfig {
    @Value("${app.mercadopago.access-token}")
    private String mercadoPagoAccessToken;
    @Value("${app.mercadopago.preference.back-urls.success}")
    private String mercadoPagoPreferenceBackUrlSuccess;
    @Value("${app.mercadopago.preference.back-urls.pending}")
    private String mercadoPagoPreferenceBackUrlPending;
    @Value("${app.mercadopago.preference.back-urls.failure}")
    private String mercadoPagoPreferenceBackUrlFailure;


    @Bean
    public String mercadoPagoAccessToken() {
        return mercadoPagoAccessToken;
    }
    @Bean
    public String mercadoPagoPreferenceBackUrlSuccess() {
        return mercadoPagoPreferenceBackUrlSuccess;
    }
    @Bean
    public String mercadoPagoPreferenceBackUrlPending() {
        return mercadoPagoPreferenceBackUrlPending;
    }
    @Bean
    public String mercadoPagoPreferenceBackUrlFailure() {
        return mercadoPagoPreferenceBackUrlFailure;
    }

}
