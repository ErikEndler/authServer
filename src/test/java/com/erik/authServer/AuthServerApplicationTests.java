package com.erik.authServer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthServerApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Verifica se o contexto da aplicação é carregado corretamente
		assertNotNull(applicationContext, "The application context should have loaded.");
	}

	@Test
	void testClientServiceBean() {
		// Verifica se o bean ClientService é carregado corretamente
		Object clientService = applicationContext.getBean("clientService");
		assertNotNull(clientService, "The ClientService bean should have been loaded.");
	}

	@Test
	void testCustomUserDetailsServiceBean() {
		// Verifica se o bean CustomUserDetailsService é carregado corretamente
		Object customUserDetailsService = applicationContext.getBean("customUserDetailsService");
		assertNotNull(customUserDetailsService, "The CustomUserDetailsService bean should have been loaded.");
	}
}