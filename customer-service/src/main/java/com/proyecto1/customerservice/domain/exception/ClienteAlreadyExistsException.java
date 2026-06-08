package com.proyecto1.customerservice.domain.exception;

public class ClienteAlreadyExistsException extends RuntimeException {

    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}