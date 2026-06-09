package com.proyecto1.accountservice.application.dto;

import java.util.List;

public class ReporteEstadoCuentaResponse {

    private Long clienteId;
    private String codigoCliente;
    private String nombre;
    private String identificacion;
    private Boolean estadoCliente;
    private List<CuentaReporteResponse> cuentas;

    public ReporteEstadoCuentaResponse() {
    }

    public ReporteEstadoCuentaResponse(
            Long clienteId,
            String codigoCliente,
            String nombre,
            String identificacion,
            Boolean estadoCliente,
            List<CuentaReporteResponse> cuentas) {
        this.clienteId = clienteId;
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estadoCliente = estadoCliente;
        this.cuentas = cuentas;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public Boolean getEstadoCliente() {
        return estadoCliente;
    }

    public List<CuentaReporteResponse> getCuentas() {
        return cuentas;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setEstadoCliente(Boolean estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    public void setCuentas(List<CuentaReporteResponse> cuentas) {
        this.cuentas = cuentas;
    }
}
