package com.proyecto1.customerservice.application.dto;

public class ClienteResponse {

    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String clienteId;
    private Boolean estado;

    public ClienteResponse() {
        // Constructor requerido por Jackson para deserializar el cuerpo JSON de la
        // petición.
    }

    public ClienteResponse(
            Long id,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String clienteId,
            Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.clienteId = clienteId;
        this.estado = estado;
    }

    public Long getId() {
        return id;
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

    public Boolean getEstado() {
        return estado;
    }
}