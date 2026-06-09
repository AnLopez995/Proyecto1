package com.proyecto1.accountservice.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoReporteResponse {

    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;

    public MovimientoReporteResponse() {
    }

    public MovimientoReporteResponse(
            LocalDateTime fecha,
            String tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo) {
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
