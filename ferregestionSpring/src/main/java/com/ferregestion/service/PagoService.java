package com.ferregestion.service;

import com.ferregestion.dto.request.PagoRequestDTO;
import com.ferregestion.dto.response.PagoResponseDTO;
import com.ferregestion.entity.Pago;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.PagoMapper;
import com.ferregestion.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    public PagoService(PagoRepository pagoRepository, PagoMapper pagoMapper) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
    }

    public List<PagoResponseDTO> listarTodos() {
        return pagoRepository.findAll().stream()
                .map(pagoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO buscarPorId(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + id + " no encontrado"));
        return pagoMapper.toResponseDTO(pago);
    }

    public PagoResponseDTO guardar(PagoRequestDTO pagoDTO) {
        Pago pago = pagoMapper.toEntity(pagoDTO);
        Pago pagoGuardado = pagoRepository.save(pago);
        return pagoMapper.toResponseDTO(pagoGuardado);
    }

    public PagoResponseDTO actualizar(Integer id, PagoRequestDTO pagoDTO) {
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + id + " no encontrado"));

        pagoMapper.updateEntityFromDTO(pagoDTO, pagoExistente);
        Pago pagoActualizado = pagoRepository.save(pagoExistente);
        return pagoMapper.toResponseDTO(pagoActualizado);
    }

    public void eliminar(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + id + " no encontrado"));
        pagoRepository.delete(pago);
    }
}