package com.proyecto1.accountservice.infrastructure.persistence;

import com.proyecto1.accountservice.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdAndFechaBetween(
            Long cuentaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin);

}