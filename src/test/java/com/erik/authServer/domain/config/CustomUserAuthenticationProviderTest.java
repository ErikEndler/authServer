package com.erik.authServer.domain.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserAuthenticationProviderTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private CustomUserAuthenticationProvider customUserAuthenticationProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_Success() {
        UserDetails userDetails = User.withUsername("user")
                                      .password("password")
                                      .authorities("ROLE_USER")
                                      .build();
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "password");

        assertDoesNotThrow(() -> customUserAuthenticationProvider.authenticate(authenticationToken));
        verify(customUserDetailsService, times(1)).loadUserByUsername("user");
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        UserDetails userDetails = User.withUsername("user")
                                      .password("password")
                                      .authorities("ROLE_USER")
                                      .build();
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "wrong_password");

        assertThrows(BadCredentialsException.class, () -> customUserAuthenticationProvider.authenticate(authenticationToken));
        verify(customUserDetailsService, times(1)).loadUserByUsername("user");
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(customUserDetailsService.loadUserByUsername(anyString())).thenThrow(new BadCredentialsException("User not found"));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "password");

        assertThrows(BadCredentialsException.class, () -> customUserAuthenticationProvider.authenticate(authenticationToken));
        verify(customUserDetailsService, times(1)).loadUserByUsername("user");
    }
}