package com.ferregestion.mapper;

import com.ferregestion.dto.request.ProductoRequestDTO;
import com.ferregestion.dto.response.ProductoResponseDTO;
import com.ferregestion.entity.Producto;
import com.ferregestion.repository.GrupoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductoMapper {

    private final GrupoRepository grupoRepository;
    private final GrupoMapper grupoMapper;

    public ProductoMapper(GrupoRepository grupoRepository,
                          GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.grupoMapper = grupoMapper;
    }

    public Producto toEntity(ProductoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Producto producto = Producto.builder()
                .descripcion(dto.getDescripcion())
                .iva(dto.getIva() != null ? dto.getIva() : new BigDecimal("19.00"))
                .precioCompra(dto.getPrecioCompra() != null ? dto.getPrecioCompra() : BigDecimal.ZERO)
                .precioVenta(dto.getPrecioVenta() != null ? dto.getPrecioVenta() : BigDecimal.ZERO)
                .stock(dto.getStock() != null ? dto.getStock() : 0)
                .build();

        // Cargar grupo si viene en el DTO y no está vacío
        if (dto.getCodigoGrupo() != null && !dto.getCodigoGrupo().trim().isEmpty()) {
            grupoRepository.findById(dto.getCodigoGrupo())
                    .ifPresent(producto::setGrupo);
        }

        return producto;
    }

    public ProductoResponseDTO toResponseDTO(Producto entity) {
        if (entity == null) {
            return null;
        }

        return ProductoResponseDTO.builder()
                .idProducto(entity.getIdProducto())
                .descripcion(entity.getDescripcion())
                .grupo(grupoMapper.toResponseDTO(entity.getGrupo()))
                .iva(entity.getIva())
                .precioCompra(entity.getPrecioCompra())
                .precioVenta(entity.getPrecioVenta())
                .stock(entity.getStock())
                .build();
    }

    public void updateEntityFromDTO(ProductoRequestDTO dto, Producto entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDescripcion(dto.getDescripcion());
        entity.setIva(dto.getIva() != null ? dto.getIva() : new BigDecimal("19.00"));
        entity.setPrecioCompra(dto.getPrecioCompra() != null ? dto.getPrecioCompra() : BigDecimal.ZERO);
        entity.setPrecioVenta(dto.getPrecioVenta() != null ? dto.getPrecioVenta() : BigDecimal.ZERO);
        entity.setStock(dto.getStock() != null ? dto.getStock() : 0);

        // Actualizar grupo
        if (dto.getCodigoGrupo() != null && !dto.getCodigoGrupo().trim().isEmpty()) {
            grupoRepository.findById(dto.getCodigoGrupo())
                    .ifPresent(entity::setGrupo);
        } else {
            entity.setGrupo(null);
        }
    }
}