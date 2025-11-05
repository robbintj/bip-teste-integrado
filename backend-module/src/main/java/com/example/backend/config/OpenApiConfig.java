package com.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI.
 * Documentação automática da API disponível em: /swagger-ui.html
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Benefícios")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de benefícios com funcionalidades de CRUD completo
                                e transferência de valores entre benefícios.
                                
                                **Características principais:**
                                - CRUD completo de benefícios
                                - Transferência atômica com validações
                                - Pessimistic locking para evitar race conditions
                                - Validação de saldo e regras de negócio
                                - Tratamento centralizado de exceções
                                - Arquitetura em camadas (Controller → Service → EJB → JPA)
                                """)
                        .contact(new Contact()
                                .name("Dev Senior Team")
                                .email("dev@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}

