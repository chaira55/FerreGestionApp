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

    public Clase buscarPorId(String codigoClase) {  // CAMBIO: Integer → String
        return claseRepository.findById(codigoClase)
                .orElseThrow(() -> new ResourceNotFoundException("Clase con código " + codigoClase + " no encontrada"));
    }

    public Clase guardar(Clase clase) {
        return claseRepository.save(clase);
    }

    public Clase actualizar(String codigoClase, Clase claseActualizada) {  // CAMBIO: Integer → String
        Clase claseExistente = buscarPorId(codigoClase);
        claseExistente.setNombre(claseActualizada.getNombre());
        // ELIMINADO: setDescripcion
        return claseRepository.save(claseExistente);
    }

    public void eliminar(String codigoClase) {  // CAMBIO: Integer → String
        Clase clase = buscarPorId(codigoClase);
        claseRepository.delete(clase);
    }
}