package com.ferregestion.controller;

import com.ferregestion.entity.Pago;
import com.ferregestion.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Listar todos los pagos
    @GetMapping
    public List<Pago> listarTodos() {
        return pagoService.listarTodos();
    }

    // Buscar pago por ID
    @GetMapping("/{id}")
    public Pago buscarPorId(@PathVariable Integer id) {
        return pagoService.buscarPorId(id);
    }

    // Crear nuevo pago
    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        Pago nuevoPago = pagoService.guardar(pago);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    // Actualizar pago existente
    @PutMapping("/{id}")
    public Pago actualizar(@PathVariable Integer id, @RequestBody Pago pagoActualizado) {
        return pagoService.actualizar(id, pagoActualizado);
    }

    // Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
