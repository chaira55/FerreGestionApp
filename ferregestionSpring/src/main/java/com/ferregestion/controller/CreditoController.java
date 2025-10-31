package com.ferregestion.controller;

import com.ferregestion.dto.request.CreditoRequestDTO;
import com.ferregestion.dto.response.CreditoResponseDTO;
import com.ferregestion.service.CreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/creditos")
@Tag(name = "Créditos", description = "API para gestión de créditos")
public class CreditoController {

    private final CreditoService creditoService;

    public CreditoController(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    @Operation(summary = "Listar todos los créditos (sin paginación)")
    @GetMapping("/all")
    public ResponseEntity<List<CreditoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(creditoService.listarTodos());
    }

    @Operation(summary = "Listar créditos con paginación")
    @GetMapping
    public ResponseEntity<Page<CreditoResponseDTO>> listarCreditosPaginado(
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCredito") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(creditoService.listarTodosPaginado(page, size, sortBy, direction));
    }

    @Operation(summary = "Buscar créditos por cliente")
    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<Page<CreditoResponseDTO>> buscarPorCliente(
            @PathVariable Integer cedula,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(creditoService.buscarPorCliente(cedula, page, size));
    }

    @Operation(summary = "Filtrar créditos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<CreditoResponseDTO>> filtrarPorEstado(
            @PathVariable String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(creditoService.filtrarPorEstado(estado, page, size));
    }

    @Operation(summary = "Listar créditos activos (no pagados)")
    @GetMapping("/activos")
    public ResponseEntity<Page<CreditoResponseDTO>> listarCreditosActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(creditoService.listarCreditosActivos(page, size));
    }

    @Operation(summary = "Listar créditos pendientes (con saldo > 0)")
    @GetMapping("/pendientes")
    public ResponseEntity<List<CreditoResponseDTO>> listarCreditosPendientes() {
        return ResponseEntity.ok(creditoService.listarCreditosPendientes());
    }

    @Operation(summary = "Calcular deuda total de un cliente")
    @GetMapping("/deuda/{cedula}")
    public ResponseEntity<BigDecimal> calcularDeudaCliente(@PathVariable Integer cedula) {
        return ResponseEntity.ok(creditoService.calcularDeudaCliente(cedula));
    }

    @Operation(summary = "Buscar crédito por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CreditoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(creditoService.buscarPorId(id));
    }

    @Operation(summary = "Crear nuevo crédito")
    @PostMapping
    public ResponseEntity<CreditoResponseDTO> crear(@Valid @RequestBody CreditoRequestDTO creditoDTO) {
        CreditoResponseDTO nuevoCredito = creditoService.guardar(creditoDTO);
        return new ResponseEntity<>(nuevoCredito, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar crédito existente")
    @PutMapping("/{id}")
    public ResponseEntity<CreditoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CreditoRequestDTO creditoDTO) {
        return ResponseEntity.ok(creditoService.actualizar(id, creditoDTO));
    }

    @Operation(summary = "Eliminar crédito")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            System.out.println("🗑️ API: Solicitud de eliminación de crédito ID: " + id);

            // Obtener información del crédito antes de eliminarlo
            CreditoResponseDTO credito = creditoService.buscarPorId(id);

            // Eliminar el crédito
            creditoService.eliminar(id);

            System.out.println("✅ API: Crédito eliminado exitosamente");

            // Respuesta con información
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crédito eliminado exitosamente");
            response.put("creditoEliminado", Map.of(
                    "id", credito.getIdCredito(),
                    "cliente", credito.getNombreCliente(),
                    "monto", credito.getMontoTotal()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ API: Error al eliminar crédito: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al eliminar el crédito: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}