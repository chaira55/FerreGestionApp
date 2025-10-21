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

    // Listar todos los grupos
    @GetMapping
    public List<Grupo> listarTodos() {
        return grupoService.listarTodos();
    }

    // Buscar grupo por ID
    @GetMapping("/{id}")
    public Grupo buscarPorId(@PathVariable Integer id) {
        return grupoService.buscarPorId(id);
    }

    // Crear nuevo grupo
    @PostMapping
    public ResponseEntity<Grupo> crear(@RequestBody Grupo grupo) {
        Grupo nuevoGrupo = grupoService.guardar(grupo);
        return new ResponseEntity<>(nuevoGrupo, HttpStatus.CREATED);
    }

    // Actualizar grupo existente
    @PutMapping("/{id}")
    public Grupo actualizar(@PathVariable Integer id, @RequestBody Grupo grupoActualizado) {
        return grupoService.actualizar(id, grupoActualizado);
    }

    // Eliminar grupo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        grupoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
