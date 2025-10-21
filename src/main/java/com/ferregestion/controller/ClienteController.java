package com.ferregestion.controller;

import com.ferregestion.entity.Cliente;
import com.ferregestion.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Listar todos los clientes
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    // Buscar cliente por cédula
    @GetMapping("/{cedula}")
    public Cliente buscarPorCedula(@PathVariable Integer cedula) {
        return clienteService.buscarPorCedula(cedula);
    }

    // Crear nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardar(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    // Actualizar cliente existente
    @PutMapping("/{cedula}")
    public Cliente actualizar(@PathVariable Integer cedula, @RequestBody Cliente clienteActualizado) {
        return clienteService.actualizar(cedula, clienteActualizado);
    }

    // Eliminar cliente
    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer cedula) {
        clienteService.eliminar(cedula);
        return ResponseEntity.noContent().build();
    }
}
