package com.erik.authServer.domain.config;

import com.erik.authServer.domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    private final ClientService clientService;

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return clientService;
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8080")
                .build();
    }
}
