package com.proyecto1.customerservice.application.dto;

import jakarta.validation.constraints.NotNull;

public class EstadoClienteRequest {

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    public EstadoClienteRequest() {
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
