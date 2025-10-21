package com.ferregestion.service;

import com.ferregestion.entity.Grupo;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.GrupoRepository;
import org.springframework.stereotype.Service;

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

    public Grupo buscarPorId(Integer idGrupo) {
        return grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo con ID " + idGrupo + " no encontrado"));
    }

    public Grupo guardar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public Grupo actualizar(Integer idGrupo, Grupo grupoActualizado) {
        Grupo grupoExistente = buscarPorId(idGrupo);
        grupoExistente.setNombre(grupoActualizado.getNombre());
        grupoExistente.setDescripcion(grupoActualizado.getDescripcion());
        grupoExistente.setClase(grupoActualizado.getClase());
        return grupoRepository.save(grupoExistente);
    }

    public void eliminar(Integer idGrupo) {
        Grupo grupo = buscarPorId(idGrupo);
        grupoRepository.delete(grupo);
    }
}
