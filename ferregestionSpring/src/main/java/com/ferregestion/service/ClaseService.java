package com.ferregestion.service;

import com.ferregestion.entity.Clase;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ClaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public List<Clase> listarTodos() {
        return claseRepository.findAll();
    }

    public Clase buscarPorId(Integer idClase) {
        return claseRepository.findById(idClase)
                .orElseThrow(() -> new ResourceNotFoundException("Clase con ID " + idClase + " no encontrada"));
    }

    public Clase guardar(Clase clase) {
        return claseRepository.save(clase);
    }

    public Clase actualizar(Integer idClase, Clase claseActualizada) {
        Clase claseExistente = buscarPorId(idClase);
        claseExistente.setNombre(claseActualizada.getNombre());
        claseExistente.setDescripcion(claseActualizada.getDescripcion());
        return claseRepository.save(claseExistente);
    }

    public void eliminar(Integer idClase) {
        Clase clase = buscarPorId(idClase);
        claseRepository.delete(clase);
    }
}
