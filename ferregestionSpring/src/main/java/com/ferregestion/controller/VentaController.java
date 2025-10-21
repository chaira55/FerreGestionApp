package com.ferregestion.controller;

import com.ferregestion.entity.Venta;
import com.ferregestion.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Listar todas las ventas
    @GetMapping
    public List<Venta> listarTodas() {
        return ventaService.listarTodos();
    }

    // Buscar venta por ID
    @GetMapping("/{id}")
    public Venta buscarPorId(@PathVariable Integer id) {
        return ventaService.buscarPorId(id);
    }

    // Crear nueva venta
    @PostMapping
    public ResponseEntity<Venta> crear(@RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.guardar(venta);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    // Actualizar venta existente
    @PutMapping("/{id}")
    public Venta actualizar(@PathVariable Integer id, @RequestBody Venta ventaActualizada) {
        return ventaService.actualizar(id, ventaActualizada);
    }

    // Eliminar venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
