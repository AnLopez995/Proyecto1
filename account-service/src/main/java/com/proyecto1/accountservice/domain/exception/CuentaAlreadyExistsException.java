package com.proyecto1.accountservice.domain.exception;

public class CuentaAlreadyExistsException extends RuntimeException {

    public CuentaAlreadyExistsException(String message) {
        super(message);
    }
}