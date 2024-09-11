package com.erik.authServer.domain.service;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public interface ClientService extends RegisteredClientRepository {
    boolean isClientSecretValid(String clientId, String clientSecret);
}