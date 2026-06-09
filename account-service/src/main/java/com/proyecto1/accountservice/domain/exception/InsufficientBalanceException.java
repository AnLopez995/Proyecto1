package com.proyecto1.accountservice.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Saldo no disponible");
    }
}