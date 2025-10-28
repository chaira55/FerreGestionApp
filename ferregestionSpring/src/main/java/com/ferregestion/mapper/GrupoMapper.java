package com.ferregestion.mapper;

import com.ferregestion.dto.request.GrupoRequestDTO;
import com.ferregestion.dto.response.GrupoResponseDTO;
import com.ferregestion.entity.Grupo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GrupoMapper {

    public Grupo toEntity(GrupoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Grupo.builder()
                .codigoGrupo(dto.getCodigoGrupo())
                .nombre(dto.getNombre())
                .iva(dto.getIva() != null ? dto.getIva() : new BigDecimal("19.00"))
                .build();
    }

    public GrupoResponseDTO toResponseDTO(Grupo entity) {
        if (entity == null) {
            return null;
        }

        return GrupoResponseDTO.builder()
                .codigoGrupo(entity.getCodigoGrupo())
                .nombre(entity.getNombre())
                .iva(entity.getIva())
                .build();
    }

    public void updateEntityFromDTO(GrupoRequestDTO dto, Grupo entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
        entity.setIva(dto.getIva());
    }
}