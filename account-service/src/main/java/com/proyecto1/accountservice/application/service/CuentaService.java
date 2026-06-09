package com.proyecto1.accountservice.application.service;

import com.proyecto1.accountservice.application.dto.CuentaRequest;
import com.proyecto1.accountservice.application.dto.CuentaResponse;
import com.proyecto1.accountservice.application.dto.EstadoCuentaRequest;
import com.proyecto1.accountservice.domain.exception.ClienteNotFoundException;
import com.proyecto1.accountservice.domain.exception.CuentaAlreadyExistsException;
import com.proyecto1.accountservice.domain.exception.CuentaNotFoundException;
import com.proyecto1.accountservice.domain.model.Cuenta;
import com.proyecto1.accountservice.infrastructure.persistence.ClienteReadModelRepository;
import com.proyecto1.accountservice.infrastructure.persistence.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteReadModelRepository clienteReadModelRepository;

    public CuentaService(
            CuentaRepository cuentaRepository,
            ClienteReadModelRepository clienteReadModelRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteReadModelRepository = clienteReadModelRepository;
    }

    @Transactional
    public CuentaResponse crearCuenta(CuentaRequest request) {
        validarCuentaNueva(request);

        Cuenta cuenta = new Cuenta(
                request.getNumeroCuenta(),
                request.getTipoCuenta(),
                request.getSaldoInicial(),
                request.getEstado(),
                request.getClienteId());

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return mapToResponse(cuentaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> listarCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponse obtenerCuentaPorId(Long id) {
        Cuenta cuenta = buscarCuentaPorId(id);
        return mapToResponse(cuenta);
    }

    @Transactional
    public CuentaResponse actualizarCuenta(Long id, CuentaRequest request) {
        Cuenta cuenta = buscarCuentaPorId(id);

        validarDuplicadosAlActualizar(cuenta, request);

        validarClienteExiste(request.getClienteId());

        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setEstado(request.getEstado());
        cuenta.setClienteId(request.getClienteId());

        /*
         * Nota técnica:
         * No modificamos saldoDisponible con el saldoInicial durante actualización,
         * porque saldoDisponible representa el estado actual de la cuenta después
         * de movimientos. Si lo cambiáramos manualmente, romperíamos la trazabilidad
         * contable.
         */

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return mapToResponse(cuentaActualizada);
    }

    @Transactional
    public CuentaResponse actualizarEstado(Long id, EstadoCuentaRequest request) {
        Cuenta cuenta = buscarCuentaPorId(id);
        cuenta.setEstado(request.getEstado());

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return mapToResponse(cuentaActualizada);
    }

    private void validarCuentaNueva(CuentaRequest request) {
        if (cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta())) {
            throw new CuentaAlreadyExistsException(
                    "Ya existe una cuenta con número: " + request.getNumeroCuenta());
        }

        validarClienteExiste(request.getClienteId());
    }

    private void validarDuplicadosAlActualizar(Cuenta cuentaActual, CuentaRequest request) {
        cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .filter(cuenta -> !cuenta.getId().equals(cuentaActual.getId()))
                .ifPresent(cuenta -> {
                    throw new CuentaAlreadyExistsException(
                            "Ya existe una cuenta con número: " + request.getNumeroCuenta());
                });
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteReadModelRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException(clienteId);
        }
    }

    private Cuenta buscarCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
    }

    private CuentaResponse mapToResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                cuenta.getClienteId());
    }
}
