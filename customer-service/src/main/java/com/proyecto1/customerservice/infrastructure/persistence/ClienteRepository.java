package com.proyecto1.customerservice.infrastructure.persistence;

import com.proyecto1.customerservice.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByClienteId(String clienteId);

    Optional<Cliente> findByIdentificacion(String identificacion);

    boolean existsByClienteId(String clienteId);

    boolean existsByIdentificacion(String identificacion);
}