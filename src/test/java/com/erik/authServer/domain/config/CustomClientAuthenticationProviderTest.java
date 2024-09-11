package com.erik.authServer.domain.config;

import com.erik.authServer.domain.service.impl.ClientAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomClientAuthenticationProviderTest {

    @Mock
    private ClientAuthenticationService clientAuthenticationService;

    @InjectMocks
    private CustomClientAuthenticationProvider customClientAuthenticationProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_ClientCredentialsFlow_Success() {
        RegisteredClient registeredClient = RegisteredClient.withId("1")
                .clientId("client-id")
                .clientSecret("encoded-secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        when(clientAuthenticationService.authenticate(anyString(), anyString())).thenReturn(registeredClient);

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("grant_type", "client_credentials");
        OAuth2ClientAuthenticationToken authenticationToken = new OAuth2ClientAuthenticationToken(
                "client-id", ClientAuthenticationMethod.CLIENT_SECRET_BASIC, "raw-secret", additionalParameters);

        assertDoesNotThrow(() -> customClientAuthenticationProvider.authenticate(authenticationToken));
        verify(clientAuthenticationService, times(1)).authenticate("client-id", "raw-secret");
    }

    @Test
    void testAuthenticate_ClientNotFound() {
        when(clientAuthenticationService.authenticate(anyString(), anyString())).thenThrow(new OAuth2AuthenticationException(new OAuth2Error("invalid_client", "Invalid client", null)));

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("grant_type", "client_credentials");
        OAuth2ClientAuthenticationToken authenticationToken = new OAuth2ClientAuthenticationToken(
                "client-id", ClientAuthenticationMethod.CLIENT_SECRET_BASIC, "raw-secret", additionalParameters);

        assertThrows(OAuth2AuthenticationException.class, () -> customClientAuthenticationProvider.authenticate(authenticationToken));
        verify(clientAuthenticationService, times(1)).authenticate("client-id", "raw-secret");
    }

    @Test
    void testAuthenticate_InvalidClientSecret() {
        when(clientAuthenticationService.authenticate(anyString(), anyString())).thenThrow(new OAuth2AuthenticationException(new OAuth2Error("invalid_client", "Invalid client secret", null)));

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("grant_type", "client_credentials");
        OAuth2ClientAuthenticationToken authenticationToken = new OAuth2ClientAuthenticationToken(
                "client-id", ClientAuthenticationMethod.CLIENT_SECRET_BASIC, "raw-secret", additionalParameters);

        assertThrows(OAuth2AuthenticationException.class, () -> customClientAuthenticationProvider.authenticate(authenticationToken));
        verify(clientAuthenticationService, times(1)).authenticate("client-id", "raw-secret");
    }
}