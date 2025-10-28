package com.ferregestion.mapper;

import com.ferregestion.dto.request.ProductoRequestDTO;
import com.ferregestion.dto.response.ProductoResponseDTO;
import com.ferregestion.entity.Clase;
import com.ferregestion.entity.Grupo;
import com.ferregestion.entity.Producto;
import com.ferregestion.repository.ClaseRepository;
import com.ferregestion.repository.GrupoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductoMapper {

    private final ClaseRepository claseRepository;
    private final GrupoRepository grupoRepository;
    private final ClaseMapper claseMapper;
    private final GrupoMapper grupoMapper;

    public ProductoMapper(ClaseRepository claseRepository,
                          GrupoRepository grupoRepository,
                          ClaseMapper claseMapper,
                          GrupoMapper grupoMapper) {
        this.claseRepository = claseRepository;
        this.grupoRepository = grupoRepository;
        this.claseMapper = claseMapper;
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

        // Cargar clase si viene en el DTO
        if (dto.getCodigoClase() != null) {
            claseRepository.findById(dto.getCodigoClase())
                    .ifPresent(producto::setClase);
        }

        // Cargar grupo si viene en el DTO
        if (dto.getCodigoGrupo() != null) {
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
                .clase(claseMapper.toResponseDTO(entity.getClase()))
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
        entity.setIva(dto.getIva());
        entity.setPrecioCompra(dto.getPrecioCompra());
        entity.setPrecioVenta(dto.getPrecioVenta());
        entity.setStock(dto.getStock());

        // Actualizar clase
        if (dto.getCodigoClase() != null) {
            claseRepository.findById(dto.getCodigoClase())
                    .ifPresent(entity::setClase);
        } else {
            entity.setClase(null);
        }

        // Actualizar grupo
        if (dto.getCodigoGrupo() != null) {
            grupoRepository.findById(dto.getCodigoGrupo())
                    .ifPresent(entity::setGrupo);
        } else {
            entity.setGrupo(null);
        }
    }
}