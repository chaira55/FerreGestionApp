package com.ferregestion.controller;

import com.ferregestion.entity.Clase;
import com.ferregestion.service.ClaseService;
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

    @GetMapping
    public List<Clase> listarTodas() {
        return claseService.listarTodos();
    }

    @GetMapping("/{codigo}")  // CAMBIO: id → codigo
    public Clase buscarPorCodigo(@PathVariable String codigo) {  // CAMBIO: Integer → String
        return claseService.buscarPorId(codigo);
    }

    @PostMapping
    public ResponseEntity<Clase> crear(@RequestBody Clase clase) {
        Clase nuevaClase = claseService.guardar(clase);
        return new ResponseEntity<>(nuevaClase, HttpStatus.CREATED);
    }

    @PutMapping("/{codigo}")  // CAMBIO: id → codigo
    public Clase actualizar(@PathVariable String codigo, @RequestBody Clase claseActualizada) {  // CAMBIO: Integer → String
        return claseService.actualizar(codigo, claseActualizada);
    }

    @DeleteMapping("/{codigo}")  // CAMBIO: id → codigo
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {  // CAMBIO: Integer → String
        claseService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}