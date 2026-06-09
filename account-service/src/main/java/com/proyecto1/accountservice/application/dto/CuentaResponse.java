package com.proyecto1.accountservice.application.dto;

import java.math.BigDecimal;

public class CuentaResponse {

    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private Boolean estado;
    private Long clienteId;

    public CuentaResponse() {
    }

    public CuentaResponse(
            Long id,
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            Boolean estado,
            Long clienteId) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoDisponible;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    public Long getId() {
        return id;
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

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public Boolean getEstado() {
        return estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}