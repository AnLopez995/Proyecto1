package com.proyecto1.customerservice.infrastructure.web;

import com.proyecto1.customerservice.application.dto.ClienteRequest;
import com.proyecto1.customerservice.application.dto.ClienteResponse;
import com.proyecto1.customerservice.application.dto.EstadoClienteRequest;
import com.proyecto1.customerservice.application.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crearCliente(@Valid @RequestBody ClienteRequest request) {
        return clienteService.crearCliente(request);
    }

    @GetMapping
    public List<ClienteResponse> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public ClienteResponse obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        return clienteService.actualizarCliente(id, request);
    }

    @PatchMapping("/{id}/estado")
    public ClienteResponse actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoClienteRequest request) {
        return clienteService.actualizarEstado(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }
}