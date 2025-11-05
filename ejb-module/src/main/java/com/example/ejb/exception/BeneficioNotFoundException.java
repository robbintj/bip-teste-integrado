package com.example.ejb.exception;
import jakarta.ejb.ApplicationException;
/**
 * Exceção lançada quando um benefício não é encontrado.
 * ApplicationException força rollback em transações EJB.
 */
@ApplicationException(rollback = true)
public class BeneficioNotFoundException extends RuntimeException {
    private final Long beneficioId;
    public BeneficioNotFoundException(Long beneficioId) {
        super(String.format("Benefício com ID %d não encontrado", beneficioId));
        this.beneficioId = beneficioId;
    }
    public Long getBeneficioId() {
        return beneficioId;
    }
}
