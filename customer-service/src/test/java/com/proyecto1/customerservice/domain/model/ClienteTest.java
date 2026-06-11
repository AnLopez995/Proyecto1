package com.proyecto1.customerservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deberiaCrearClienteConDatosValidos() {
        Cliente cliente = new Cliente(
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "CLI-001",
                "password-encriptado",
                true);

        assertNull(cliente.getId());
        assertEquals("Jose Lema", cliente.getNombre());
        assertEquals("Masculino", cliente.getGenero());
        assertEquals(30, cliente.getEdad());
        assertEquals("1234567890", cliente.getIdentificacion());
        assertEquals("Otavalo sn y principal", cliente.getDireccion());
        assertEquals("0987654321", cliente.getTelefono());
        assertEquals("CLI-001", cliente.getClienteId());
        assertEquals("password-encriptado", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }

    @Test
    void deberiaActualizarDatosDelCliente() {
        Cliente cliente = new Cliente(
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "CLI-001",
                "password-encriptado",
                true);

        cliente.actualizarDatos(
                "Jose Actualizado",
                "Masculino",
                31,
                "1234567890",
                "Nueva direccion",
                "0999999999",
                "nuevo-password-encriptado",
                false);

        assertEquals("Jose Actualizado", cliente.getNombre());
        assertEquals("Masculino", cliente.getGenero());
        assertEquals(31, cliente.getEdad());
        assertEquals("1234567890", cliente.getIdentificacion());
        assertEquals("Nueva direccion", cliente.getDireccion());
        assertEquals("0999999999", cliente.getTelefono());
        assertEquals("CLI-001", cliente.getClienteId());
        assertEquals("nuevo-password-encriptado", cliente.getContrasena());
        assertFalse(cliente.getEstado());
    }

    @Test
    void deberiaPermitirCambiarClienteIdContrasenaYEstado() {
        Cliente cliente = new Cliente(
                "Marianela Montalvo",
                "Femenino",
                28,
                "0987654321",
                "Amazonas y NNUU",
                "0999999999",
                "CLI-002",
                "password-original",
                true);

        cliente.setClienteId("CLI-002-UPDATED");
        cliente.setContrasena("password-nuevo");
        cliente.setEstado(false);

        assertEquals("CLI-002-UPDATED", cliente.getClienteId());
        assertEquals("password-nuevo", cliente.getContrasena());
        assertFalse(cliente.getEstado());
    }
}