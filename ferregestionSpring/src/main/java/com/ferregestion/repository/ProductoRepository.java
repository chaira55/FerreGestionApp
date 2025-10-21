package com.ferregestion.repository;

import com.ferregestion.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // ELIMINADO: existsByNombre (ya no existe el campo nombre)
}