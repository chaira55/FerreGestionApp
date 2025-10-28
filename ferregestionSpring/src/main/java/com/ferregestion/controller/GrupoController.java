package com.ferregestion.controller;

import com.ferregestion.dto.request.GrupoRequestDTO;
import com.ferregestion.dto.response.GrupoResponseDTO;
import com.ferregestion.service.GrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "Grupos", description = "API para gestión de grupos de productos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @Operation(summary = "Listar todos los grupos")
    @GetMapping
    public ResponseEntity<List<GrupoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(grupoService.listarTodos());
    }

    @Operation(summary = "Buscar grupo por código")
    @GetMapping("/{codigo}")
    public ResponseEntity<GrupoResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(grupoService.buscarPorId(codigo));
    }

    @Operation(summary = "Crear nuevo grupo")
    @PostMapping
    public ResponseEntity<GrupoResponseDTO> crear(@Valid @RequestBody GrupoRequestDTO grupoDTO) {
        GrupoResponseDTO nuevoGrupo = grupoService.guardar(grupoDTO);
        return new ResponseEntity<>(nuevoGrupo, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar grupo existente")
    @PutMapping("/{codigo}")
    public ResponseEntity<GrupoResponseDTO> actualizar(
            @PathVariable String codigo,
            @Valid @RequestBody GrupoRequestDTO grupoDTO) {
        return ResponseEntity.ok(grupoService.actualizar(codigo, grupoDTO));
    }

    @Operation(summary = "Eliminar grupo")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        grupoService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}