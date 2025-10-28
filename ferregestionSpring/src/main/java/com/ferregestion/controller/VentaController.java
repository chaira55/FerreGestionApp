package com.ferregestion.controller;

import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.service.VentaService;
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
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "API para gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Listar todas las ventas (sin paginación)")
    @GetMapping("/all")
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodos());
    }

    @Operation(summary = "Listar ventas con paginación")
    @GetMapping
    public ResponseEntity<Page<VentaResponseDTO>> listarVentasPaginado(
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(ventaService.listarTodosPaginado(page, size, sortBy, direction));
    }

    @Operation(summary = "Buscar ventas por cédula del cliente")
    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<Page<VentaResponseDTO>> buscarPorCliente(
            @PathVariable Integer cedula,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ventaService.buscarPorCliente(cedula, page, size));
    }

    @Operation(summary = "Buscar ventas por nombre del cliente")
    @GetMapping("/buscar")
    public ResponseEntity<Page<VentaResponseDTO>> buscarPorNombre(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ventaService.buscarPorNombreCliente(nombre, page, size));
    }

    @Operation(summary = "Filtrar ventas por tipo de pago")
    @GetMapping("/tipo-pago/{tipoPago}")
    public ResponseEntity<Page<VentaResponseDTO>> filtrarPorTipoPago(
            @PathVariable String tipoPago,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ventaService.filtrarPorTipoPago(tipoPago, page, size));
    }

    @Operation(summary = "Filtrar ventas por rango de fechas")
    @GetMapping("/fecha")
    public ResponseEntity<Page<VentaResponseDTO>> filtrarPorFechas(
            @Parameter(description = "Fecha inicio (formato: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha fin (formato: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ventaService.filtrarPorRangoFechas(fechaInicio, fechaFin, page, size));
    }

    @Operation(summary = "Filtrar ventas por rango de total")
    @GetMapping("/total")
    public ResponseEntity<Page<VentaResponseDTO>> filtrarPorTotal(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(ventaService.filtrarPorRangoTotal(min, max, page, size));
    }

    @Operation(summary = "Ventas del día")
    @GetMapping("/hoy")
    public ResponseEntity<List<VentaResponseDTO>> ventasDelDia() {
        return ResponseEntity.ok(ventaService.ventasDelDia(LocalDate.now()));
    }

    @Operation(summary = "Ventas por fecha específica")
    @GetMapping("/dia/{fecha}")
    public ResponseEntity<List<VentaResponseDTO>> ventasPorDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ventaService.ventasDelDia(fecha));
    }

    @Operation(summary = "Obtener estadísticas de ventas por rango de fechas")
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        return ResponseEntity.ok(ventaService.obtenerEstadisticas(fechaInicio, fechaFin));
    }

    @Operation(summary = "Buscar venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @Operation(summary = "Crear nueva venta")
    @PostMapping
    public ResponseEntity<VentaResponseDTO> crear(@Valid @RequestBody VentaRequestDTO ventaDTO) {
        VentaResponseDTO nuevaVenta = ventaService.guardar(ventaDTO);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar venta existente")
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody VentaRequestDTO ventaDTO) {
        return ResponseEntity.ok(ventaService.actualizar(id, ventaDTO));
    }

    @Operation(summary = "Eliminar venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}