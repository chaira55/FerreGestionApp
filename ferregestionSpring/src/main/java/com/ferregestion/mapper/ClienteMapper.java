package com.ferregestion.mapper;

import com.ferregestion.dto.request.ClienteRequestDTO;
import com.ferregestion.dto.response.ClienteResponseDTO;
import com.ferregestion.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Cliente.builder()
                .cedula(dto.getCedula())
                .nombre(dto.getNombre())
                .celular(dto.getCelular())
                .direccion(dto.getDireccion())
                .correo(dto.getCorreo())
                .build();
    }

    public ClienteResponseDTO toResponseDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }

        return ClienteResponseDTO.builder()
                .cedula(entity.getCedula())
                .nombre(entity.getNombre())
                .celular(entity.getCelular())
                .direccion(entity.getDireccion())
                .correo(entity.getCorreo())
                .build();
    }

    public void updateEntityFromDTO(ClienteRequestDTO dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
        entity.setCelular(dto.getCelular());
        entity.setDireccion(dto.getDireccion());
        entity.setCorreo(dto.getCorreo());
    }
}