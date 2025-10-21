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

    public Grupo buscarPorId(String codigoGrupo) {  // CAMBIO: Integer → String
        return grupoRepository.findById(codigoGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo con código " + codigoGrupo + " no encontrado"));
    }

    public Grupo guardar(Grupo grupo) {
        // Asegurar que el IVA tenga valor por defecto si no se proporciona
        if (grupo.getIva() == null) {
            grupo.setIva(19.00);
        }
        return grupoRepository.save(grupo);
    }

    public Grupo actualizar(String codigoGrupo, Grupo grupoActualizado) {  // CAMBIO: Integer → String
        Grupo grupoExistente = buscarPorId(codigoGrupo);
        grupoExistente.setNombre(grupoActualizado.getNombre());
        grupoExistente.setIva(grupoActualizado.getIva());  // NUEVO
        // ELIMINADO: setDescripcion
        // ELIMINADO: setClase
        return grupoRepository.save(grupoExistente);
    }

    public void eliminar(String codigoGrupo) {  // CAMBIO: Integer → String
        Grupo grupo = buscarPorId(codigoGrupo);
        grupoRepository.delete(grupo);
    }
}