package com.ferregestion.controller;

import com.ferregestion.dto.request.ClaseRequestDTO;
import com.ferregestion.dto.response.ClaseResponseDTO;
import com.ferregestion.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
@Tag(name = "Clases", description = "API para gestión de clases de productos")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @Operation(summary = "Listar todas las clases")
    @GetMapping
    public ResponseEntity<List<ClaseResponseDTO>> listarTodas() {
        return ResponseEntity.ok(claseService.listarTodos());
    }

    @Operation(summary = "Buscar clase por código")
    @GetMapping("/{codigo}")
    public ResponseEntity<ClaseResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(claseService.buscarPorId(codigo));
    }

    @Operation(summary = "Crear nueva clase")
    @PostMapping
    public ResponseEntity<ClaseResponseDTO> crear(@Valid @RequestBody ClaseRequestDTO claseDTO) {
        ClaseResponseDTO nuevaClase = claseService.guardar(claseDTO);
        return new ResponseEntity<>(nuevaClase, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar clase existente")
    @PutMapping("/{codigo}")
    public ResponseEntity<ClaseResponseDTO> actualizar(
            @PathVariable String codigo,
            @Valid @RequestBody ClaseRequestDTO claseDTO) {
        return ResponseEntity.ok(claseService.actualizar(codigo, claseDTO));
    }

    @Operation(summary = "Eliminar clase")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        claseService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}