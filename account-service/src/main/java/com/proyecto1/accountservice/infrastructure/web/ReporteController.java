package com.proyecto1.accountservice.infrastructure.web;

import com.proyecto1.accountservice.application.dto.ApiResponse;
import com.proyecto1.accountservice.application.dto.ReporteEstadoCuentaResponse;
import com.proyecto1.accountservice.application.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ReporteEstadoCuentaResponse>> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        ReporteEstadoCuentaResponse reporte = reporteService.generarReporte(
                clienteId,
                fechaInicio,
                fechaFin);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Reporte generado exitosamente",
                        reporte));
    }
}
