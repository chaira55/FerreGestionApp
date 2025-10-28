package com.ferregestion.controller;

import com.ferregestion.dto.request.ClienteRequestDTO;
import com.ferregestion.dto.response.ClienteResponseDTO;
import com.ferregestion.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos los clientes (sin paginación)")
    @GetMapping("/all")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @Operation(summary = "Listar clientes con paginación")
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listarClientesPaginado(
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cedula") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(clienteService.listarTodosPaginado(page, size, sortBy, direction));
    }

    @Operation(summary = "Buscar clientes por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> buscarPorNombre(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(clienteService.buscarPorNombre(nombre, page, size));
    }

    @Operation(summary = "Buscar cliente por cédula")
    @GetMapping("/{cedula}")
    public ResponseEntity<ClienteResponseDTO> buscarPorCedula(@PathVariable Integer cedula) {
        return ResponseEntity.ok(clienteService.buscarPorCedula(cedula));
    }

    @Operation(summary = "Crear nuevo cliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO clienteDTO) {
        ClienteResponseDTO nuevoCliente = clienteService.guardar(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar cliente existente")
    @PutMapping("/{cedula}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable Integer cedula,
            @Valid @RequestBody ClienteRequestDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.actualizar(cedula, clienteDTO));
    }

    @Operation(summary = "Eliminar cliente")
    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer cedula) {
        clienteService.eliminar(cedula);
        return ResponseEntity.noContent().build();
    }
}