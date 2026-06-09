package com.proyecto1.accountservice.domain.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long clienteId) {
        super("Cliente no encontrado en account-service con id: " + clienteId);
    }
}