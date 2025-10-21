package com.ferregestion.repository;

import com.ferregestion.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, String> {  // CAMBIO: Integer → String
}