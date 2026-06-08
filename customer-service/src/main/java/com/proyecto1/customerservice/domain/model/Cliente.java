package com.proyecto1.customerservice.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Cliente extends Persona {

    @Column(name = "cliente_id", nullable = false, unique = true, length = 50)
    private String clienteId;

    @Column(name = "contrasena", nullable = false, length = 120)
    private String contrasena;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    protected Cliente() {
    }

    public Cliente(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String clienteId,
            String contrasena,
            Boolean estado) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void actualizarDatos(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            Boolean estado) {
        setNombre(nombre);
        setGenero(genero);
        setEdad(edad);
        setIdentificacion(identificacion);
        setDireccion(direccion);
        setTelefono(telefono);
        this.contrasena = contrasena;
        this.estado = estado;
    }
}