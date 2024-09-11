package com.erik.authServer.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantTypes;
    private String scopes;
    private boolean blocked;  // novo campo
    private int failedAttempts;  // campo para contar tentativas falhadas
}