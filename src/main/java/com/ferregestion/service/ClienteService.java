package com.ferregestion.service;

import com.ferregestion.entity.Cliente;
import com.ferregestion.exception.DuplicateResourceException;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorCedula(Integer cedula) {
        return clienteRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con cédula " + cedula + " no encontrado"));
    }

    public Cliente guardar(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getCedula())) {
            throw new DuplicateResourceException("Ya existe un cliente con la cédula " + cliente.getCedula());
        }
        if (cliente.getCorreo() != null && clienteRepository.existsByCorreo(cliente.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un cliente con el correo " + cliente.getCorreo());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Integer cedula, Cliente clienteActualizado) {
        Cliente clienteExistente = buscarPorCedula(cedula);
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setCelular(clienteActualizado.getCelular());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setCorreo(clienteActualizado.getCorreo());
        return clienteRepository.save(clienteExistente);
    }

    public void eliminar(Integer cedula) {
        Cliente cliente = buscarPorCedula(cedula);
        clienteRepository.delete(cliente);
    }
}
