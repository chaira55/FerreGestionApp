package com.ferregestion.controller;

import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "API para gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las ventas")
    public ResponseEntity<List<VentaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(ventaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venta por ID")
    public ResponseEntity<VentaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva venta")
    public ResponseEntity<VentaResponseDTO> crear(@Valid @RequestBody VentaRequestDTO ventaDTO) {
        VentaResponseDTO ventaCreada = ventaService.guardar(ventaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaCreada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una venta")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}