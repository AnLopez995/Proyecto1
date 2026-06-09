package com.proyecto1.accountservice.domain.exception;

public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException(String numeroCuenta) {
        super("La cuenta " + numeroCuenta + " se encuentra inactiva");
    }
}