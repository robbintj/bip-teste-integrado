package com.example.ejb.exception;

import jakarta.ejb.ApplicationException;

/**
 * Exceção lançada quando uma transferência é inválida.
 * ApplicationException força rollback em transações EJB.
 */
@ApplicationException(rollback = true)
public class InvalidTransferException extends RuntimeException {

    public InvalidTransferException(String message) {
        super(message);
    }
}

