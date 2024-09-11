package com.erik.authServer.domain.service.impl;

import com.erik.authServer.domain.model.Client;
import com.erik.authServer.domain.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FailedAttemptsServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private FailedAttemptsService failedAttemptsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIncreaseFailedAttempts() {
        Client client = new Client();
        client.setClientId("client-id");
        client.setFailedAttempts(2);
        when(clientRepository.findByClientId(anyString())).thenReturn(client);

        failedAttemptsService.increaseFailedAttempts("client-id");

        verify(clientRepository, times(1)).save(client);
        assertTrue(client.isBlocked());
    }
}