package com.erik.authServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "your_api_key";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		System.out.println("Raw Password: " + rawPassword);
		System.out.println("Encoded Password: " + encodedPassword);
	}

}
