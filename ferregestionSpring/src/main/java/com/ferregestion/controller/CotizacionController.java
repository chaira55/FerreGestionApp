package com.ferregestion.controller;

import com.ferregestion.dto.request.CotizacionRequestDTO;
import com.ferregestion.dto.response.CotizacionResponseDTO;
import com.ferregestion.service.CotizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cotizaciones")
@Tag(name = "Cotizaciones", description = "API para gestión de cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @Operation(summary = "Listar todas las cotizaciones (sin paginación)")
    @GetMapping("/all")
    public ResponseEntity<List<CotizacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(cotizacionService.listarTodas());
    }

    @Operation(summary = "Listar cotizaciones con paginación")
    @GetMapping
    public ResponseEntity<Page<CotizacionResponseDTO>> listarCotizacionesPaginado(
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(cotizacionService.listarTodasPaginado(page, size, sortBy, direction));
    }

    @Operation(summary = "Buscar cotizaciones por cédula del cliente")
    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<Page<CotizacionResponseDTO>> buscarPorCliente(
            @PathVariable Integer cedula,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(cotizacionService.buscarPorCliente(cedula, page, size));
    }

    @Operation(summary = "Buscar cotizaciones por nombre del cliente")
    @GetMapping("/buscar")
    public ResponseEntity<Page<CotizacionResponseDTO>> buscarPorNombre(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(cotizacionService.buscarPorNombreCliente(nombre, page, size));
    }

    @Operation(summary = "Filtrar cotizaciones por rango de fechas")
    @GetMapping("/fecha")
    public ResponseEntity<Page<CotizacionResponseDTO>> filtrarPorFechas(
            @Parameter(description = "Fecha inicio (formato: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha fin (formato: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(cotizacionService.filtrarPorRangoFechas(fechaInicio, fechaFin, page, size));
    }

    @Operation(summary = "Filtrar cotizaciones por rango de total")
    @GetMapping("/total")
    public ResponseEntity<Page<CotizacionResponseDTO>> filtrarPorTotal(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(cotizacionService.filtrarPorRangoTotal(min, max, page, size));
    }

    @Operation(summary = "Cotizaciones del día")
    @GetMapping("/hoy")
    public ResponseEntity<List<CotizacionResponseDTO>> cotizacionesDelDia() {
        return ResponseEntity.ok(cotizacionService.cotizacionesDelDia(LocalDate.now()));
    }

    @Operation(summary = "Cotizaciones por fecha específica")
    @GetMapping("/dia/{fecha}")
    public ResponseEntity<List<CotizacionResponseDTO>> cotizacionesPorDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(cotizacionService.cotizacionesDelDia(fecha));
    }

    @Operation(summary = "Obtener estadísticas de cotizaciones por rango de fechas")
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        return ResponseEntity.ok(cotizacionService.obtenerEstadisticas(fechaInicio, fechaFin));
    }

    @Operation(summary = "Buscar cotización por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cotizacionService.buscarPorId(id));
    }

    @Operation(summary = "Crear nueva cotización")
    @PostMapping
    public ResponseEntity<CotizacionResponseDTO> crear(@Valid @RequestBody CotizacionRequestDTO cotizacionDTO) {
        CotizacionResponseDTO nuevaCotizacion = cotizacionService.guardar(cotizacionDTO);
        return new ResponseEntity<>(nuevaCotizacion, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar cotización existente")
    @PutMapping("/{id}")
    public ResponseEntity<CotizacionResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CotizacionRequestDTO cotizacionDTO) {
        return ResponseEntity.ok(cotizacionService.actualizar(id, cotizacionDTO));
    }

    @Operation(summary = "Eliminar cotización")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cotizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}