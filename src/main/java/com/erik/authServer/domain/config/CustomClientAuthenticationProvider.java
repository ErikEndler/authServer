package com.erik.authServer.domain.config;

import com.erik.authServer.domain.service.impl.ClientAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomClientAuthenticationProvider implements AuthenticationProvider {

    private final ClientAuthenticationService clientAuthenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof OAuth2ClientAuthenticationToken)) {
            return null;
        }

        OAuth2ClientAuthenticationToken clientAuthenticationToken = (OAuth2ClientAuthenticationToken) authentication;
        String clientId = clientAuthenticationToken.getPrincipal().toString();
        String clientSecret = clientAuthenticationToken.getCredentials().toString();
        Map<String, Object> additionalParameters = clientAuthenticationToken.getAdditionalParameters();

        log.info("Attempting to authenticate client with ID: {}", clientId);
        try {
            RegisteredClient registeredClient = clientAuthenticationService.authenticate(clientId, clientSecret);

            String grantType = (String) additionalParameters.get("grant_type");
            if (grantType != null) {
                log.info("Grant type: {}", grantType);
            }

            log.info("Client authenticated successfully with ID: {}", clientId);
            return new OAuth2ClientAuthenticationToken(registeredClient, clientAuthenticationToken.getClientAuthenticationMethod(), clientAuthenticationToken.getAdditionalParameters());
        } catch (OAuth2AuthenticationException e) {
            log.warn("Authentication failed for client ID: {}", clientId);
            throw new IllegalStateException(e.getMessage()) {
            };  // Re-throw the exception to be handled by the security framework
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }
}