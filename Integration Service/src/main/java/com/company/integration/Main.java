package com.company.integration;

import com.company.integration.module.IntegrationModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "com.company.core.model.administrative",
		"com.company.core.model.functional",
		"com.company.core.model.tools" })
@ComponentScans(value = { @ComponentScan("com.company.core.security"),@ComponentScan("com.company.core.service"), @ComponentScan("com.company.core.config")})
@EnableJpaRepositories({"com.company.core.repository"})
public class Main {

	public static void main(String[] args) {
		System.out.println("INTEGRATION is running");
		SpringApplication.run(Main.class, args);
		new IntegrationModule().start();
	}

}
