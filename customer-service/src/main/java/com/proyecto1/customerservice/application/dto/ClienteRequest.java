package com.proyecto1.customerservice.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    private String nombre;

    @Size(max = 20, message = "El género no puede superar 20 caracteres")
    private String genero;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 30, message = "La identificación no puede superar 30 caracteres")
    private String identificacion;

    @Size(max = 200, message = "La dirección no puede superar 200 caracteres")
    private String direccion;

    @Size(max = 30, message = "El teléfono no puede superar 30 caracteres")
    private String telefono;

    @NotBlank(message = "El clienteId es obligatorio")
    @Size(max = 50, message = "El clienteId no puede superar 50 caracteres")
    private String clienteId;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 120, message = "La contraseña no puede superar 120 caracteres")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    public ClienteRequest() {
        // Constructor requerido por Jackson para deserializar el cuerpo JSON de la
        // petición.
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
}