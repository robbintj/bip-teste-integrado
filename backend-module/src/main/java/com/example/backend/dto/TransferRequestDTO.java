package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisição de transferência entre benefícios.
 * Valida regras de negócio antes de chamar o EJB.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para transferência entre benefícios")
public class TransferRequestDTO {

    @NotNull(message = "ID do benefício de origem é obrigatório")
    @Schema(description = "ID do benefício origem", example = "1", required = true)
    private Long fromId;

    @NotNull(message = "ID do benefício de destino é obrigatório")
    @Schema(description = "ID do benefício destino", example = "2", required = true)
    private Long toId;

    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Schema(description = "Valor a ser transferido", example = "100.00", required = true)
    private BigDecimal amount;
}

