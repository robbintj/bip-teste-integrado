package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisicao de criacao/atualizacao de Beneficio.
 * Segue principio SOLID: SRP - responsabilidade unica de transferir dados.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criacao/atualizacao de um beneficio")
public class BeneficioRequestDTO {

    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
    @Schema(description = "Nome do beneficio", example = "Vale Alimentacao", required = true)
    private String nome;

    @Size(max = 255, message = "Descricao deve ter no maximo 255 caracteres")
    @Schema(description = "Descricao do beneficio", example = "Beneficio de alimentacao para funcionarios")
    private String descricao;

    @NotNull(message = "Valor e obrigatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Valor nao pode ser negativo")
    @Schema(description = "Valor do beneficio", example = "1000.00", required = true)
    private BigDecimal valor;

    @Schema(description = "Status do beneficio", example = "true", defaultValue = "true")
    private Boolean ativo;
}

