package com.erik.authServer.domain.service.impl;

import com.erik.authServer.domain.model.Client;
import com.erik.authServer.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FailedAttemptsService {

    private final ClientRepository clientRepository;

    @Transactional
    public void increaseFailedAttempts(String clientId) {
        log.info("Increasing failed attempts for client ID: {}", clientId);
        Client client = clientRepository.findByClientId(clientId);
        if (client != null) {
            int newFailedAttempts = client.getFailedAttempts() + 1;
            if (newFailedAttempts >= 3) {
                log.warn("Client is blocked due to too many failed attempts with ID: {}", clientId);
                client.setBlocked(true);
            }
            client.setFailedAttempts(newFailedAttempts);
            clientRepository.save(client);
        }
    }
}