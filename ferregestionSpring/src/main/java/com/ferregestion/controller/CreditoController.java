package com.ferregestion.controller;

import com.ferregestion.entity.Credito;
import com.ferregestion.service.CreditoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    private final CreditoService creditoService;

    public CreditoController(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    // Listar todos los créditos
    @GetMapping
    public List<Credito> listarTodos() {
        return creditoService.listarTodos();
    }

    // Buscar crédito por ID
    @GetMapping("/{id}")
    public ResponseEntity<Credito> buscarPorId(@PathVariable Integer id) {
        Credito credito = creditoService.buscarPorId(id);
        return credito != null ? ResponseEntity.ok(credito) : ResponseEntity.notFound().build();
    }

    // Crear nuevo crédito
    @PostMapping
    public ResponseEntity<Credito> crear(@RequestBody Credito credito) {
        Credito nuevoCredito = creditoService.guardar(credito);
        return new ResponseEntity<>(nuevoCredito, HttpStatus.CREATED);
    }

    // Actualizar crédito existente
    @PutMapping("/{id}")
    public ResponseEntity<Credito> actualizar(@PathVariable Integer id, @RequestBody Credito creditoActualizado) {
        Credito actualizado = creditoService.actualizar(id, creditoActualizado);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    // Eliminar crédito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        creditoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
