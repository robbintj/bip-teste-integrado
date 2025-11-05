package com.example.ejb.exception;

import jakarta.ejb.ApplicationException;
import java.math.BigDecimal;

/**
 * Excecao lancada quando ha saldo insuficiente para transferencia.
 * ApplicationException forca rollback em transacoes EJB.
 */
@ApplicationException(rollback = true)
public class InsufficientBalanceException extends RuntimeException {

    private final Long beneficioId;
    private final BigDecimal currentBalance;
    private final BigDecimal requiredAmount;

    public InsufficientBalanceException(Long beneficioId, BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(String.format(
            "Saldo insuficiente no beneficio ID %d. Saldo atual: R$ %.2f, Valor necessario: R$ %.2f",
            beneficioId, currentBalance, requiredAmount
        ));
        this.beneficioId = beneficioId;
        this.currentBalance = currentBalance;
        this.requiredAmount = requiredAmount;
    }

    public Long getBeneficioId() {
        return beneficioId;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public BigDecimal getRequiredAmount() {
        return requiredAmount;
    }
}

