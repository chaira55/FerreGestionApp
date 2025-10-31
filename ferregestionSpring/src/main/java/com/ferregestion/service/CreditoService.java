package com.ferregestion.service;

import com.ferregestion.dto.request.CreditoRequestDTO;
import com.ferregestion.dto.response.CreditoResponseDTO;
import com.ferregestion.entity.Credito;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.CreditoMapper;
import com.ferregestion.repository.CreditoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditoService {

    private final CreditoRepository creditoRepository;
    private final CreditoMapper creditoMapper;

    public CreditoService(CreditoRepository creditoRepository, CreditoMapper creditoMapper) {
        this.creditoRepository = creditoRepository;
        this.creditoMapper = creditoMapper;
    }

    public List<CreditoResponseDTO> listarTodos() {
        return creditoRepository.findAll().stream()
                .map(creditoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<CreditoResponseDTO> listarTodosPaginado(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return creditoRepository.findAll(pageable).map(creditoMapper::toResponseDTO);
    }

    public Page<CreditoResponseDTO> buscarPorCliente(Integer cedula, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return creditoRepository.findByClienteCedula(cedula, pageable).map(creditoMapper::toResponseDTO);
    }

    public Page<CreditoResponseDTO> filtrarPorEstado(String estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return creditoRepository.findByEstado(estado, pageable).map(creditoMapper::toResponseDTO);
    }

    public Page<CreditoResponseDTO> listarCreditosActivos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idCredito"));
        return creditoRepository.findCreditosActivos(pageable).map(creditoMapper::toResponseDTO);
    }

    public List<CreditoResponseDTO> listarCreditosPendientes() {
        return creditoRepository.findCreditosPendientes().stream()
                .map(creditoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal calcularDeudaCliente(Integer cedula) {
        BigDecimal deuda = creditoRepository.calcularDeudaCliente(cedula);
        return deuda != null ? deuda : BigDecimal.ZERO;
    }

    public CreditoResponseDTO buscarPorId(Integer id) {
        Credito credito = creditoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crédito con ID " + id + " no encontrado"));
        return creditoMapper.toResponseDTO(credito);
    }

    @Transactional
    public CreditoResponseDTO guardar(CreditoRequestDTO creditoDTO) {
        Credito credito = creditoMapper.toEntity(creditoDTO);
        Credito creditoGuardado = creditoRepository.save(credito);
        return creditoMapper.toResponseDTO(creditoGuardado);
    }

    @Transactional
    public CreditoResponseDTO actualizar(Integer id, CreditoRequestDTO creditoDTO) {
        Credito creditoExistente = creditoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crédito con ID " + id + " no encontrado"));
        creditoMapper.updateEntityFromDTO(creditoDTO, creditoExistente);
        Credito creditoActualizado = creditoRepository.save(creditoExistente);
        return creditoMapper.toResponseDTO(creditoActualizado);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!creditoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Crédito no encontrado con id: " + id);
        }
        creditoRepository.deleteById(id);
        creditoRepository.flush();
    }
}