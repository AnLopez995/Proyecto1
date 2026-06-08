package com.proyecto1.customerservice.infrastructure.messaging;

public class ClienteEvent {

    private String eventType;
    private Long id;
    private String clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;

    public ClienteEvent() {
    }

    public ClienteEvent(String eventType, Long id, String clienteId, String nombre, String identificacion,
            Boolean estado) {
        this.eventType = eventType;
        this.id = id;
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
    }

    public String getEventType() {
        return eventType;
    }

    public Long getId() {
        return id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}