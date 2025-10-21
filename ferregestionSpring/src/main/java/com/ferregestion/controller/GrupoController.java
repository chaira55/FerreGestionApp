package com.ferregestion.controller;

import com.ferregestion.entity.Grupo;
import com.ferregestion.service.GrupoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<Grupo> listarTodos() {
        return grupoService.listarTodos();
    }

    @GetMapping("/{codigo}")  // CAMBIO: id → codigo
    public Grupo buscarPorCodigo(@PathVariable String codigo) {  // CAMBIO: Integer → String
        return grupoService.buscarPorId(codigo);
    }

    @PostMapping
    public ResponseEntity<Grupo> crear(@RequestBody Grupo grupo) {
        Grupo nuevoGrupo = grupoService.guardar(grupo);
        return new ResponseEntity<>(nuevoGrupo, HttpStatus.CREATED);
    }

    @PutMapping("/{codigo}")  // CAMBIO: id → codigo
    public Grupo actualizar(@PathVariable String codigo, @RequestBody Grupo grupoActualizado) {  // CAMBIO: Integer → String
        return grupoService.actualizar(codigo, grupoActualizado);
    }

    @DeleteMapping("/{codigo}")  // CAMBIO: id → codigo
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {  // CAMBIO: Integer → String
        grupoService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}