package com.ferregestion.repository;

import com.ferregestion.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByCorreo(String correo);

    // NUEVO: Buscar por nombre
    Page<Cliente> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}