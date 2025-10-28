package com.ferregestion.service;

import com.ferregestion.entity.Grupo;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> listarTodos() {
        return grupoRepository.findAll();
    }

    public Grupo buscarPorId(String codigoGrupo) {
        return grupoRepository.findById(codigoGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo con código " + codigoGrupo + " no encontrado"));
    }

    public Grupo guardar(Grupo grupo) {
        if (grupo.getIva() == null) {
            grupo.setIva(new BigDecimal("19.00"));
        }
        return grupoRepository.save(grupo);
    }

    public Grupo actualizar(String codigoGrupo, Grupo grupoActualizado) {
        Grupo grupoExistente = buscarPorId(codigoGrupo);
        grupoExistente.setNombre(grupoActualizado.getNombre());
        grupoExistente.setIva(grupoActualizado.getIva());
        return grupoRepository.save(grupoExistente);
    }

    public void eliminar(String codigoGrupo) {
        Grupo grupo = buscarPorId(codigoGrupo);
        grupoRepository.delete(grupo);
    }
}