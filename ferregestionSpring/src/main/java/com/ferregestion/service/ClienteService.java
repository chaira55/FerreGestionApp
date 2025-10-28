package com.ferregestion.service;

import com.ferregestion.dto.request.ClienteRequestDTO;
import com.ferregestion.dto.response.ClienteResponseDTO;
import com.ferregestion.entity.Cliente;
import com.ferregestion.exception.DuplicateResourceException;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.ClienteMapper;
import com.ferregestion.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // NUEVO: Paginación
    public Page<ClienteResponseDTO> listarTodosPaginado(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return clienteRepository.findAll(pageable)
                .map(clienteMapper::toResponseDTO);
    }

    // NUEVO: Buscar por nombre
    public Page<ClienteResponseDTO> buscarPorNombre(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(clienteMapper::toResponseDTO);
    }

    public ClienteResponseDTO buscarPorCedula(Integer cedula) {
        Cliente cliente = clienteRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con cédula " + cedula + " no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO guardar(ClienteRequestDTO clienteDTO) {
        if (clienteRepository.existsById(clienteDTO.getCedula())) {
            throw new DuplicateResourceException("Ya existe un cliente con la cédula " + clienteDTO.getCedula());
        }
        if (clienteDTO.getCorreo() != null && clienteRepository.existsByCorreo(clienteDTO.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un cliente con el correo " + clienteDTO.getCorreo());
        }

        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteGuardado);
    }

    public ClienteResponseDTO actualizar(Integer cedula, ClienteRequestDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con cédula " + cedula + " no encontrado"));

        clienteMapper.updateEntityFromDTO(clienteDTO, clienteExistente);
        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return clienteMapper.toResponseDTO(clienteActualizado);
    }

    public void eliminar(Integer cedula) {
        Cliente cliente = clienteRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con cédula " + cedula + " no encontrado"));
        clienteRepository.delete(cliente);
    }
}