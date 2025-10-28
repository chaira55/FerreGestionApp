package com.ferregestion.repository;

import com.ferregestion.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar por descripción (parcial, ignorando mayúsculas)
    Page<Producto> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);

    // Filtrar por clase
    Page<Producto> findByClaseCodigoClase(String codigoClase, Pageable pageable);

    // Filtrar por grupo
    Page<Producto> findByGrupoCodigoGrupo(String codigoGrupo, Pageable pageable);

    // Filtrar por rango de precio
    Page<Producto> findByPrecioVentaBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    // Stock bajo
    List<Producto> findByStockLessThanEqual(int stock);
}