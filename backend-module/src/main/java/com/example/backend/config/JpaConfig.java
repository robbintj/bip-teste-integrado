package com.example.backend.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuracao JPA para Spring Boot.
 * Habilita gerenciamento de transacoes e escaneia entidades do modulo EJB.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.backend")
@EntityScan(basePackages = {"com.example.ejb", "com.example.backend"})
public class JpaConfig {
    // Configuracao automatica do Spring Boot com scan explicito de entidades
}

