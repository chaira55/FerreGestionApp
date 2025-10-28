package com.ferregestion.service;

import com.ferregestion.dto.request.ClaseRequestDTO;
import com.ferregestion.dto.response.ClaseResponseDTO;
import com.ferregestion.entity.Clase;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.ClaseMapper;
import com.ferregestion.repository.ClaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ClaseMapper claseMapper;

    public ClaseService(ClaseRepository claseRepository, ClaseMapper claseMapper) {
        this.claseRepository = claseRepository;
        this.claseMapper = claseMapper;
    }

    public List<ClaseResponseDTO> listarTodos() {
        return claseRepository.findAll().stream()
                .map(claseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClaseResponseDTO buscarPorId(String codigoClase) {
        Clase clase = claseRepository.findById(codigoClase)
                .orElseThrow(() -> new ResourceNotFoundException("Clase con código " + codigoClase + " no encontrada"));
        return claseMapper.toResponseDTO(clase);
    }

    public ClaseResponseDTO guardar(ClaseRequestDTO claseDTO) {
        Clase clase = claseMapper.toEntity(claseDTO);
        Clase claseGuardada = claseRepository.save(clase);
        return claseMapper.toResponseDTO(claseGuardada);
    }

    public ClaseResponseDTO actualizar(String codigoClase, ClaseRequestDTO claseDTO) {
        Clase claseExistente = claseRepository.findById(codigoClase)
                .orElseThrow(() -> new ResourceNotFoundException("Clase con código " + codigoClase + " no encontrada"));

        claseMapper.updateEntityFromDTO(claseDTO, claseExistente);
        Clase claseActualizada = claseRepository.save(claseExistente);
        return claseMapper.toResponseDTO(claseActualizada);
    }

    public void eliminar(String codigoClase) {
        Clase clase = claseRepository.findById(codigoClase)
                .orElseThrow(() -> new ResourceNotFoundException("Clase con código " + codigoClase + " no encontrada"));
        claseRepository.delete(clase);
    }
}