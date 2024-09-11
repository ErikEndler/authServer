package com.erik.authServer.domain.service.impl;

import com.erik.authServer.domain.model.Client;
import com.erik.authServer.domain.repository.ClientRepository;
import com.erik.authServer.domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        log.info("Saving registered client with ID: {}", registeredClient.getClientId());
        Client client = new Client();
        client.setClientId(registeredClient.getClientId());
        client.setClientSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
        client.setRedirectUri(String.join(",", registeredClient.getRedirectUris()));
        client.setGrantTypes(String.join(",", registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue).toArray(String[]::new)));
        client.setScopes(String.join(",", registeredClient.getScopes()));
        clientRepository.save(client);
        log.info("Registered client saved successfully with ID: {}", client.getClientId());
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findById(String id) {
        log.info("Finding registered client by ID: {}", id);
        return clientRepository.findById(Long.valueOf(id))
                .map(this::toRegisteredClient)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findByClientId(String clientId) {
        log.info("Finding registered client by client ID: {}", clientId);
        Client client = clientRepository.findByClientId(clientId);
        if (client != null && client.isBlocked()) {
            log.warn("Client is blocked with ID: {}", clientId);
            throw new IllegalStateException("Client is blocked");
        }
        return client != null ? toRegisteredClient(client) : null;
    }

    @Override
    public boolean isClientSecretValid(String clientId, String clientSecret) {
        RegisteredClient registeredClient = findByClientId(clientId);
        return registeredClient != null && passwordEncoder.matches(clientSecret, registeredClient.getClientSecret());
    }

    private RegisteredClient toRegisteredClient(Client client) {
        return RegisteredClient.withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(
                        Arrays.stream(client.getGrantTypes().split(","))
                                .map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet())))
                .scopes(scopes -> scopes.addAll(Arrays.asList(client.getScopes().split(","))))
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
    }
}
