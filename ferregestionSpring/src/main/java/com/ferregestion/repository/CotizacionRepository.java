package com.ferregestion.repository;

import com.ferregestion.entity.Cotizacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {

    // Buscar por cliente (cédula)
    Page<Cotizacion> findByClienteCedula(Integer cedula, Pageable pageable);

    // Buscar por nombre del cliente
    Page<Cotizacion> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Buscar por rango de fechas
    Page<Cotizacion> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);

    // Buscar por rango de totales
    Page<Cotizacion> findByTotalBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    // Cotizaciones del día
    List<Cotizacion> findByFecha(LocalDate fecha);

    // NUEVO: Total de cotizaciones en un rango
    @Query("SELECT SUM(c.total) FROM Cotizacion c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calcularTotalCotizacionesPorRango(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // NUEVO: Contar cotizaciones por cliente
    @Query("SELECT c.cliente.nombre, COUNT(c) FROM Cotizacion c GROUP BY c.cliente.nombre")
    List<Object[]> contarCotizacionesPorCliente();
}