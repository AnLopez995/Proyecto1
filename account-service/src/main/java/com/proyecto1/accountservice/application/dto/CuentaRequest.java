package com.proyecto1.accountservice.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CuentaRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 30, message = "El número de cuenta no puede superar 30 caracteres")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Size(max = 30, message = "El tipo de cuenta no puede superar 30 caracteres")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    public CuentaRequest() {
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public Boolean getEstado() {
        return estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
