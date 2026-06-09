package com.proyecto1.customerservice.infrastructure.web;

import com.proyecto1.customerservice.application.dto.ApiResponse;
import com.proyecto1.customerservice.application.dto.ClienteRequest;
import com.proyecto1.customerservice.application.dto.ClienteResponse;
import com.proyecto1.customerservice.application.dto.EstadoClienteRequest;
import com.proyecto1.customerservice.application.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<ClienteResponse>> crearCliente(
            @Valid @RequestBody ClienteRequest request) {
        ClienteResponse cliente = clienteService.crearCliente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Cliente creado exitosamente",
                        cliente));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> listarClientes() {
        List<ClienteResponse> clientes = clienteService.listarClientes();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Clientes consultados exitosamente",
                        clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> obtenerClientePorId(
            @PathVariable Long id) {
        ClienteResponse cliente = clienteService.obtenerClientePorId(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Cliente consultado exitosamente",
                        cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        ClienteResponse cliente = clienteService.actualizarCliente(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Cliente actualizado exitosamente",
                        cliente));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoClienteRequest request) {
        ClienteResponse cliente = clienteService.actualizarEstado(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Estado del cliente actualizado exitosamente",
                        cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Cliente eliminado exitosamente",
                        null));
    }
}