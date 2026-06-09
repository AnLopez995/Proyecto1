package com.proyecto1.accountservice.application.service;

import com.proyecto1.accountservice.application.dto.MovimientoRequest;
import com.proyecto1.accountservice.application.dto.MovimientoResponse;
import com.proyecto1.accountservice.domain.exception.CuentaNotFoundException;
import com.proyecto1.accountservice.domain.exception.InactiveAccountException;
import com.proyecto1.accountservice.domain.exception.InsufficientBalanceException;
import com.proyecto1.accountservice.domain.exception.InvalidMovementValueException;
import com.proyecto1.accountservice.domain.model.Cuenta;
import com.proyecto1.accountservice.domain.model.Movimiento;
import com.proyecto1.accountservice.infrastructure.persistence.CuentaRepository;
import com.proyecto1.accountservice.infrastructure.persistence.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    private static final String TIPO_DEPOSITO = "DEPOSITO";
    private static final String TIPO_RETIRO = "RETIRO";

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(
            MovimientoRepository movimientoRepository,
            CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) {
        validarValorMovimiento(request.getValor());

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .orElseThrow(() -> new CuentaNotFoundException(request.getNumeroCuenta()));

        validarCuentaActiva(cuenta);

        BigDecimal valor = request.getValor();
        String tipoMovimiento;
        BigDecimal nuevoSaldo;

        if (esDeposito(valor)) {
            tipoMovimiento = TIPO_DEPOSITO;
            cuenta.acreditar(valor);
            nuevoSaldo = cuenta.getSaldoDisponible();
        } else {
            tipoMovimiento = TIPO_RETIRO;
            BigDecimal valorRetiro = valor.abs();

            validarSaldoDisponible(cuenta, valorRetiro);

            cuenta.debitar(valorRetiro);
            nuevoSaldo = cuenta.getSaldoDisponible();
        }

        Movimiento movimiento = new Movimiento(
                LocalDateTime.now(),
                tipoMovimiento,
                valor,
                nuevoSaldo,
                cuenta);

        cuentaRepository.save(cuenta);
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        return mapToResponse(movimientoGuardado);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validarValorMovimiento(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidMovementValueException();
        }
    }

    private boolean esDeposito(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }

    private void validarCuentaActiva(Cuenta cuenta) {
        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new InactiveAccountException(cuenta.getNumeroCuenta());
        }
    }

    private void validarSaldoDisponible(Cuenta cuenta, BigDecimal valorRetiro) {
        if (cuenta.getSaldoDisponible().compareTo(valorRetiro) < 0) {
            throw new InsufficientBalanceException();
        }
    }

    private MovimientoResponse mapToResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getCuenta().getNumeroCuenta());
    }
}