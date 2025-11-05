package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para resposta de Benefício.
 * Separa a representação da API da entidade JPA (DTO Pattern).
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de um benefício")
public class BeneficioResponseDTO {

    @Schema(description = "ID do benefício", example = "1")
    private Long id;

    @Schema(description = "Nome do benefício", example = "Vale Alimentação")
    private String nome;

    @Schema(description = "Descrição do benefício", example = "Benefício de alimentação")
    private String descricao;

    @Schema(description = "Valor do benefício", example = "1000.00")
    private BigDecimal valor;

    @Schema(description = "Status do benefício", example = "true")
    private Boolean ativo;

    @Schema(description = "Versão para controle de concorrência", example = "0")
    private Long version;
}

