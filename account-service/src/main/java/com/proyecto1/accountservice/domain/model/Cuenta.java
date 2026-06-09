package com.proyecto1.accountservice.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 30)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 30)
    private String tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_disponible", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    protected Cuenta() {
    }

    public Cuenta(
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            Boolean estado,
            Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoInicial;
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

    public void acreditar(BigDecimal valor) {
        this.saldoDisponible = this.saldoDisponible.add(valor);
    }

    public void debitar(BigDecimal valor) {
        this.saldoDisponible = this.saldoDisponible.subtract(valor);
    }
}