package com.proyecto1.accountservice.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void deberiaCrearCuentaInicializandoSaldoDisponibleConSaldoInicial() {
        Cuenta cuenta = new Cuenta(
                "478758",
                "Ahorros",
                new BigDecimal("2000.00"),
                true,
                1L);

        assertNull(cuenta.getId());
        assertEquals("478758", cuenta.getNumeroCuenta());
        assertEquals("Ahorros", cuenta.getTipoCuenta());
        assertEquals(0, new BigDecimal("2000.00").compareTo(cuenta.getSaldoInicial()));
        assertEquals(0, new BigDecimal("2000.00").compareTo(cuenta.getSaldoDisponible()));
        assertTrue(cuenta.getEstado());
        assertEquals(1L, cuenta.getClienteId());
    }

    @Test
    void deberiaAcreditarValorAlSaldoDisponible() {
        Cuenta cuenta = new Cuenta(
                "478758",
                "Ahorros",
                new BigDecimal("2000.00"),
                true,
                1L);

        cuenta.acreditar(new BigDecimal("600.00"));

        assertEquals(0, new BigDecimal("2600.00").compareTo(cuenta.getSaldoDisponible()));
    }

    @Test
    void deberiaDebitarValorDelSaldoDisponible() {
        Cuenta cuenta = new Cuenta(
                "478758",
                "Ahorros",
                new BigDecimal("2000.00"),
                true,
                1L);

        cuenta.debitar(new BigDecimal("575.00"));

        assertEquals(0, new BigDecimal("1425.00").compareTo(cuenta.getSaldoDisponible()));
    }

    @Test
    void deberiaActualizarDatosBasicosDeCuenta() {
        Cuenta cuenta = new Cuenta(
                "478758",
                "Ahorros",
                new BigDecimal("2000.00"),
                true,
                1L);

        cuenta.setNumeroCuenta("999999");
        cuenta.setTipoCuenta("Corriente");
        cuenta.setSaldoInicial(new BigDecimal("3000.00"));
        cuenta.setSaldoDisponible(new BigDecimal("2500.00"));
        cuenta.setEstado(false);
        cuenta.setClienteId(2L);

        assertEquals("999999", cuenta.getNumeroCuenta());
        assertEquals("Corriente", cuenta.getTipoCuenta());
        assertEquals(0, new BigDecimal("3000.00").compareTo(cuenta.getSaldoInicial()));
        assertEquals(0, new BigDecimal("2500.00").compareTo(cuenta.getSaldoDisponible()));
        assertFalse(cuenta.getEstado());
        assertEquals(2L, cuenta.getClienteId());
    }
}
