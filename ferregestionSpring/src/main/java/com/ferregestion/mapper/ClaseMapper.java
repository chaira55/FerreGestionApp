package com.ferregestion.mapper;

import com.ferregestion.dto.request.ClaseRequestDTO;
import com.ferregestion.dto.response.ClaseResponseDTO;
import com.ferregestion.entity.Clase;
import org.springframework.stereotype.Component;

@Component
public class ClaseMapper {

    public Clase toEntity(ClaseRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Clase.builder()
                .codigoClase(dto.getCodigoClase())
                .nombre(dto.getNombre())
                .build();
    }

    public ClaseResponseDTO toResponseDTO(Clase entity) {
        if (entity == null) {
            return null;
        }

        return ClaseResponseDTO.builder()
                .codigoClase(entity.getCodigoClase())
                .nombre(entity.getNombre())
                .build();
    }

    public void updateEntityFromDTO(ClaseRequestDTO dto, Clase entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
    }
}