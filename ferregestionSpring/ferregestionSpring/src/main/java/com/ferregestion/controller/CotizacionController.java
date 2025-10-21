package com.ferregestion.controller;

import com.ferregestion.entity.Cotizacion;
import com.ferregestion.service.CotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    // Listar todas las cotizaciones
    @GetMapping
    public List<Cotizacion> listarTodas() {
        return cotizacionService.listarTodas();
    }

    // Buscar cotización por ID
    @GetMapping("/{id}")
    public Cotizacion buscarPorId(@PathVariable Integer id) {
        return cotizacionService.buscarPorId(id);
    }

    // Crear nueva cotización
    @PostMapping
    public ResponseEntity<Cotizacion> crear(@RequestBody Cotizacion cotizacion) {
        Cotizacion nuevaCotizacion = cotizacionService.guardar(cotizacion);
        return new ResponseEntity<>(nuevaCotizacion, HttpStatus.CREATED);
    }

    // Actualizar cotización existente
    @PutMapping("/{id}")
    public Cotizacion actualizar(@PathVariable Integer id, @RequestBody Cotizacion cotizacionActualizada) {
        return cotizacionService.actualizar(id, cotizacionActualizada);
    }

    // Eliminar cotización
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cotizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
