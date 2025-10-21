package com.ferregestion.repository;

import com.ferregestion.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, String> {  // CAMBIO: Integer → String
}