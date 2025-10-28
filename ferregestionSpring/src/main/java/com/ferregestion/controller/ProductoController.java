package com.ferregestion.controller;

import com.ferregestion.dto.request.ProductoRequestDTO;
import com.ferregestion.dto.response.ProductoResponseDTO;
import com.ferregestion.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar todos los productos (sin paginación)")
    @GetMapping("/all")
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(summary = "Listar productos con paginación y ordenamiento")
    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductosPaginado(
            @Parameter(description = "Número de página (inicia en 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de elementos por página")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo para ordenar (ej: descripcion, precioVenta, stock)")
            @RequestParam(defaultValue = "idProducto") String sortBy,

            @Parameter(description = "Dirección de ordenamiento (asc o desc)")
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(productoService.listarTodosPaginado(page, size, sortBy, direction));
    }

    @Operation(summary = "Buscar productos por descripción")
    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoResponseDTO>> buscarPorDescripcion(
            @Parameter(description = "Texto a buscar en la descripción")
            @RequestParam String descripcion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(productoService.buscarPorDescripcion(descripcion, page, size));
    }

    @Operation(summary = "Filtrar productos por grupo")
    @GetMapping("/grupo/{codigoGrupo}")
    public ResponseEntity<Page<ProductoResponseDTO>> filtrarPorGrupo(
            @PathVariable String codigoGrupo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(productoService.filtrarPorGrupo(codigoGrupo, page, size));
    }

    @Operation(summary = "Listar productos con stock bajo")
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponseDTO>> listarStockBajo(
            @Parameter(description = "Stock mínimo")
            @RequestParam(defaultValue = "10") int stockMinimo) {

        return ResponseEntity.ok(productoService.listarStockBajo(stockMinimo));
    }

    @Operation(summary = "Buscar producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(summary = "Crear nuevo producto")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO productoDTO) {
        ProductoResponseDTO nuevoProducto = productoService.guardar(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizar(id, productoDTO));
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}