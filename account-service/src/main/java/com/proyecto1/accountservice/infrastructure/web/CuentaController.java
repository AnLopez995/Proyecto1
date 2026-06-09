package com.proyecto1.accountservice.infrastructure.web;

import com.proyecto1.accountservice.application.dto.ApiResponse;
import com.proyecto1.accountservice.application.dto.CuentaRequest;
import com.proyecto1.accountservice.application.dto.CuentaResponse;
import com.proyecto1.accountservice.application.dto.EstadoCuentaRequest;
import com.proyecto1.accountservice.application.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CuentaResponse>> crearCuenta(
            @Valid @RequestBody CuentaRequest request) {
        CuentaResponse cuenta = cuentaService.crearCuenta(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Cuenta creada exitosamente",
                        cuenta));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CuentaResponse>>> listarCuentas() {
        List<CuentaResponse> cuentas = cuentaService.listarCuentas();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Cuentas consultadas exitosamente",
                        cuentas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponse>> obtenerCuentaPorId(
            @PathVariable Long id) {
        CuentaResponse cuenta = cuentaService.obtenerCuentaPorId(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Cuenta consultada exitosamente",
                        cuenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponse>> actualizarCuenta(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequest request) {
        CuentaResponse cuenta = cuentaService.actualizarCuenta(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Cuenta actualizada exitosamente",
                        cuenta));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<CuentaResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoCuentaRequest request) {
        CuentaResponse cuenta = cuentaService.actualizarEstado(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Estado de la cuenta actualizado exitosamente",
                        cuenta));
    }
}
