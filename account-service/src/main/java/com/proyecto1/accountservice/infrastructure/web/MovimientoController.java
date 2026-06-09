package com.proyecto1.accountservice.infrastructure.web;

import com.proyecto1.accountservice.application.dto.ApiResponse;
import com.proyecto1.accountservice.application.dto.MovimientoRequest;
import com.proyecto1.accountservice.application.dto.MovimientoResponse;
import com.proyecto1.accountservice.application.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MovimientoResponse>> registrarMovimiento(
            @Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse movimiento = movimientoService.registrarMovimiento(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Movimiento registrado exitosamente",
                        movimiento));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovimientoResponse>>> listarMovimientos() {
        List<MovimientoResponse> movimientos = movimientoService.listarMovimientos();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Movimientos consultados exitosamente",
                        movimientos));
    }
}