package com.proyecto1.accountservice.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class CuentaReporteResponse {

    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private Boolean estado;
    private List<MovimientoReporteResponse> movimientos;

    public CuentaReporteResponse() {
    }

    public CuentaReporteResponse(
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            Boolean estado,
            List<MovimientoReporteResponse> movimientos) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoDisponible;
        this.estado = estado;
        this.movimientos = movimientos;
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

    public List<MovimientoReporteResponse> getMovimientos() {
        return movimientos;
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

    public void setMovimientos(List<MovimientoReporteResponse> movimientos) {
        this.movimientos = movimientos;
    }
}