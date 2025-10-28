package com.ferregestion.service;

import com.ferregestion.dto.request.GrupoRequestDTO;
import com.ferregestion.dto.response.GrupoResponseDTO;
import com.ferregestion.entity.Grupo;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.GrupoMapper;
import com.ferregestion.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final GrupoMapper grupoMapper;

    public GrupoService(GrupoRepository grupoRepository, GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.grupoMapper = grupoMapper;
    }

    public List<GrupoResponseDTO> listarTodos() {
        return grupoRepository.findAll().stream()
                .map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public GrupoResponseDTO buscarPorId(String codigoGrupo) {
        Grupo grupo = grupoRepository.findById(codigoGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo con código " + codigoGrupo + " no encontrado"));
        return grupoMapper.toResponseDTO(grupo);
    }

    public GrupoResponseDTO guardar(GrupoRequestDTO grupoDTO) {
        Grupo grupo = grupoMapper.toEntity(grupoDTO);
        Grupo grupoGuardado = grupoRepository.save(grupo);
        return grupoMapper.toResponseDTO(grupoGuardado);
    }

    public GrupoResponseDTO actualizar(String codigoGrupo, GrupoRequestDTO grupoDTO) {
        Grupo grupoExistente = grupoRepository.findById(codigoGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo con código " + codigoGrupo + " no encontrado"));

        grupoMapper.updateEntityFromDTO(grupoDTO, grupoExistente);
        Grupo grupoActualizado = grupoRepository.save(grupoExistente);
        return grupoMapper.toResponseDTO(grupoActualizado);
    }

    public void eliminar(String codigoGrupo) {
        Grupo grupo = grupoRepository.findById(codigoGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo con código " + codigoGrupo + " no encontrado"));
        grupoRepository.delete(grupo);
    }
}