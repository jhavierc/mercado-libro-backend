package com.mercadolibro.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "app.oauth")
@Configuration
@Data
@NoArgsConstructor
public class OauthProviders {
    Map<String, OatuhProvider> providers;

    @Data
    @NoArgsConstructor
    public static class OatuhProvider {
        private String authorizationEndpoint;
        private String tokenEndpoint;
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String scope;
    }
}
