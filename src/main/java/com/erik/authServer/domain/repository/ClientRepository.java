package com.erik.authServer.domain.repository;

import com.erik.authServer.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
  Client findByClientId(String clientId);
}