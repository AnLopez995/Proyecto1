package com.proyecto1.accountservice.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente_read_model")
public class ClienteReadModel {

    @Id
    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "codigo_cliente", nullable = false, length = 50)
    private String codigoCliente;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "identificacion", nullable = false, length = 30)
    private String identificacion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    protected ClienteReadModel() {
    }

    public ClienteReadModel(
            Long clienteId,
            String codigoCliente,
            String nombre,
            String identificacion,
            Boolean estado) {
        this.clienteId = clienteId;
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
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

    public Boolean getEstado() {
        return estado;
    }

    public void actualizar(String codigoCliente, String nombre, String identificacion, Boolean estado) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
    }
}