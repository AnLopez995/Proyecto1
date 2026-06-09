package com.proyecto1.accountservice.application.dto;

import jakarta.validation.constraints.NotNull;

public class EstadoCuentaRequest {

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    public EstadoCuentaRequest() {
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}