package com.erik.authServer.domain.service.impl;

import com.erik.authServer.domain.model.Client;
import com.erik.authServer.domain.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByClientId_ClientExists() {
        Client client = new Client();
        client.setId(1L);
        client.setClientId("client-id");
        client.setClientSecret("encoded-secret");
        client.setGrantTypes("client_credentials");
        client.setScopes("read,write");
        when(clientRepository.findByClientId(anyString())).thenReturn(client);

        RegisteredClient registeredClient = clientService.findByClientId("client-id");

        assertNotNull(registeredClient);
        assertEquals("client-id", registeredClient.getClientId());
        verify(clientRepository, times(1)).findByClientId("client-id");
    }

    @Test
    void testFindByClientId_ClientDoesNotExist() {
        when(clientRepository.findByClientId(anyString())).thenReturn(null);

        RegisteredClient registeredClient = clientService.findByClientId("client-id");

        assertNull(registeredClient);
        verify(clientRepository, times(1)).findByClientId("client-id");
    }

    @Test
    void testIsClientSecretValid() {
        Client client = new Client();
        client.setId(1L); // Certifique-se de que o ID é configurado
        client.setClientId("client-id");
        client.setClientSecret("encoded-secret");
        client.setGrantTypes("client_credentials");
        client.setScopes("read,write");
        when(clientRepository.findByClientId(anyString())).thenReturn(client);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = clientService.isClientSecretValid("client-id", "raw-secret");

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches("raw-secret", "encoded-secret");
    }
}
