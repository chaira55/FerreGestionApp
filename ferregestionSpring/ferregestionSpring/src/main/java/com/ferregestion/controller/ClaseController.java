package com.ferregestion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    // Listar todas las clases
    @GetMapping
    public List<Clase> listarTodas() {
        return claseService.listarTodos();
    }

    // Buscar clase por ID
    @GetMapping("/{id}")
    public Clase buscarPorId(@PathVariable Integer id) {
        return claseService.buscarPorId(id);
    }

    // Crear nueva clase
    @PostMapping
    public ResponseEntity<Clase> crear(@RequestBody Clase clase) {
        Clase nuevaClase = claseService.guardar(clase);
        return new ResponseEntity<>(nuevaClase, HttpStatus.CREATED);
    }

    // Actualizar clase existente
    @PutMapping("/{id}")
    public Clase actualizar(@PathVariable Integer id, @RequestBody Clase claseActualizada) {
        return claseService.actualizar(id, claseActualizada);
    }

    // Eliminar clase
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        claseService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
