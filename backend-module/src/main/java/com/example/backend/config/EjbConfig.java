package com.example.backend.config;

import com.example.ejb.BeneficioEjbService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracao para integracao do EJB com Spring.
 * Cria bean do EJB Service injetando EntityManager do Spring.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Configuration
public class EjbConfig {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Cria bean do EJB Service.
     * Injeta EntityManager manualmente pois EJB precisa de EntityManager.
     */
    @Bean
    public BeneficioEjbService beneficioEjbService() {
        BeneficioEjbService service = new BeneficioEjbService();
        // Injetar EntityManager via reflexão (workaround para usar EJB no Spring)
        try {
            java.lang.reflect.Field emField = BeneficioEjbService.class.getDeclaredField("em");
            emField.setAccessible(true);
            emField.set(service, entityManager);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar EntityManager no EJB Service", e);
        }
        return service;
    }
}

