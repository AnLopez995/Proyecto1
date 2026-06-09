package com.proyecto1.accountservice.application.service;

import com.proyecto1.accountservice.application.dto.CuentaReporteResponse;
import com.proyecto1.accountservice.application.dto.MovimientoReporteResponse;
import com.proyecto1.accountservice.application.dto.ReporteEstadoCuentaResponse;
import com.proyecto1.accountservice.domain.exception.ClienteNotFoundException;
import com.proyecto1.accountservice.domain.model.ClienteReadModel;
import com.proyecto1.accountservice.domain.model.Cuenta;
import com.proyecto1.accountservice.domain.model.Movimiento;
import com.proyecto1.accountservice.infrastructure.persistence.ClienteReadModelRepository;
import com.proyecto1.accountservice.infrastructure.persistence.CuentaRepository;
import com.proyecto1.accountservice.infrastructure.persistence.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteService {

    private final ClienteReadModelRepository clienteReadModelRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteService(
            ClienteReadModelRepository clienteReadModelRepository,
            CuentaRepository cuentaRepository,
            MovimientoRepository movimientoRepository) {
        this.clienteReadModelRepository = clienteReadModelRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public ReporteEstadoCuentaResponse generarReporte(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        ClienteReadModel cliente = clienteReadModelRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        List<CuentaReporteResponse> cuentasReporte = cuentas.stream()
                .map(cuenta -> mapCuentaReporte(cuenta, fechaInicio, fechaFin))
                .toList();

        return new ReporteEstadoCuentaResponse(
                cliente.getClienteId(),
                cliente.getCodigoCliente(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado(),
                cuentasReporte);
    }

    private CuentaReporteResponse mapCuentaReporte(
            Cuenta cuenta,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                cuenta.getId(),
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(LocalTime.MAX));

        List<MovimientoReporteResponse> movimientosReporte = movimientos.stream()
                .map(this::mapMovimientoReporte)
                .toList();

        return new CuentaReporteResponse(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                movimientosReporte);
    }

    private MovimientoReporteResponse mapMovimientoReporte(Movimiento movimiento) {
        return new MovimientoReporteResponse(
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo());
    }
}