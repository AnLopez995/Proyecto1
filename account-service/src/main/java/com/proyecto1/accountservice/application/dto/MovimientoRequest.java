package com.proyecto1.accountservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MovimientoRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El valor del movimiento es obligatorio")
    private BigDecimal valor;

    public MovimientoRequest() {
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
