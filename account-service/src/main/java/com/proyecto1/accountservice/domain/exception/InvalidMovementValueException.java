package com.proyecto1.accountservice.domain.exception;

public class InvalidMovementValueException extends RuntimeException {

    public InvalidMovementValueException() {
        super("El valor del movimiento debe ser diferente de cero");
    }
}