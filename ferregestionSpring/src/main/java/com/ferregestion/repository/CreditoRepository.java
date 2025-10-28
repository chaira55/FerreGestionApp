package com.ferregestion.repository;

import com.ferregestion.entity.Credito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Integer> {

    // Buscar por cliente
    Page<Credito> findByClienteCedula(Integer cedula, Pageable pageable);

    // Buscar por estado
    Page<Credito> findByEstado(String estado, Pageable pageable);

    // Créditos activos (estado diferente a PAGADO)
    @Query("SELECT c FROM Credito c WHERE c.estado != 'PAGADO'")
    Page<Credito> findCreditosActivos(Pageable pageable);

    // Créditos por vencer (con saldo pendiente)
    @Query("SELECT c FROM Credito c WHERE c.saldoPendiente > 0")
    List<Credito> findCreditosPendientes();

    // Total de deuda por cliente
    @Query("SELECT SUM(c.saldoPendiente) FROM Credito c WHERE c.cliente.cedula = :cedula AND c.estado != 'PAGADO'")
    BigDecimal calcularDeudaCliente(Integer cedula);
}