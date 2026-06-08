package com.proyecto1.customerservice.application.service;

import com.proyecto1.customerservice.application.dto.ClienteRequest;
import com.proyecto1.customerservice.application.dto.ClienteResponse;
import com.proyecto1.customerservice.application.dto.EstadoClienteRequest;
import com.proyecto1.customerservice.domain.exception.ClienteAlreadyExistsException;
import com.proyecto1.customerservice.domain.exception.ClienteNotFoundException;
import com.proyecto1.customerservice.domain.model.Cliente;
import com.proyecto1.customerservice.infrastructure.messaging.ClienteEventPublisher;
import com.proyecto1.customerservice.infrastructure.persistence.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher clienteEventPublisher;

    public ClienteService(ClienteRepository clienteRepository, ClienteEventPublisher clienteEventPublisher) {
        this.clienteRepository = clienteRepository;
        this.clienteEventPublisher = clienteEventPublisher;
    }

    @Transactional
    public ClienteResponse crearCliente(ClienteRequest request) {
        validarClienteNuevo(request);

        Cliente cliente = new Cliente(
                request.getNombre(),
                request.getGenero(),
                request.getEdad(),
                request.getIdentificacion(),
                request.getDireccion(),
                request.getTelefono(),
                request.getClienteId(),
                request.getContrasena(),
                request.getEstado());

        Cliente clienteGuardado = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteCreated(clienteGuardado);

        return mapToResponse(clienteGuardado);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerClientePorId(Long id) {
        Cliente cliente = buscarClientePorId(id);
        return mapToResponse(cliente);
    }

    @Transactional
    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {
        Cliente cliente = buscarClientePorId(id);

        validarDuplicadosAlActualizar(cliente, request);

        cliente.setClienteId(request.getClienteId());
        cliente.actualizarDatos(
                request.getNombre(),
                request.getGenero(),
                request.getEdad(),
                request.getIdentificacion(),
                request.getDireccion(),
                request.getTelefono(),
                request.getContrasena(),
                request.getEstado());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteUpdated(clienteActualizado);

        return mapToResponse(clienteActualizado);
    }

    @Transactional
    public ClienteResponse actualizarEstado(Long id, EstadoClienteRequest request) {
        Cliente cliente = buscarClientePorId(id);
        cliente.setEstado(request.getEstado());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteUpdated(clienteActualizado);

        return mapToResponse(clienteActualizado);
    }

    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = buscarClientePorId(id);
        clienteRepository.delete(cliente);
        clienteEventPublisher.publishClienteDeleted(cliente);
    }

    private Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    private void validarClienteNuevo(ClienteRequest request) {
        if (clienteRepository.existsByClienteId(request.getClienteId())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con clienteId: " + request.getClienteId());
        }

        if (clienteRepository.existsByIdentificacion(request.getIdentificacion())) {
            throw new ClienteAlreadyExistsException(
                    "Ya existe un cliente con identificación: " + request.getIdentificacion());
        }
    }

    private void validarDuplicadosAlActualizar(Cliente clienteActual, ClienteRequest request) {
        clienteRepository.findByClienteId(request.getClienteId())
                .filter(cliente -> !cliente.getId().equals(clienteActual.getId()))
                .ifPresent(cliente -> {
                    throw new ClienteAlreadyExistsException(
                            "Ya existe un cliente con clienteId: " + request.getClienteId());
                });

        clienteRepository.findByIdentificacion(request.getIdentificacion())
                .filter(cliente -> !cliente.getId().equals(clienteActual.getId()))
                .ifPresent(cliente -> {
                    throw new ClienteAlreadyExistsException(
                            "Ya existe un cliente con identificación: " + request.getIdentificacion());
                });
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getClienteId(),
                cliente.getEstado());
    }
}
