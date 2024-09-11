package com.erik.authServer.domain.service.impl;

import com.erik.authServer.domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientAuthenticationService {

    private final ClientService clientService;
    private final FailedAttemptsService failedAttemptsService;

    public RegisteredClient authenticate(String clientId, String clientSecret) {
        RegisteredClient registeredClient = clientService.findByClientId(clientId);
        if (registeredClient == null) {
            log.warn("Client not found: {}", clientId);
            failedAttemptsService.increaseFailedAttempts(clientId);
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client", "Invalid client", null));
        }

        if (!clientService.isClientSecretValid(clientId, clientSecret)) {
            log.warn("Invalid client secret for client ID: {}", clientId);
            failedAttemptsService.increaseFailedAttempts(clientId);
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client", "Invalid client secret", null));
        }

        return registeredClient;
    }
}