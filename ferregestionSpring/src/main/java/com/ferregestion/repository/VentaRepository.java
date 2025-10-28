package com.ferregestion.repository;

import com.ferregestion.entity.Venta;
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
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // Buscar por cliente (cédula)
    Page<Venta> findByClienteCedula(Integer cedula, Pageable pageable);

    // Buscar por nombre del cliente
    Page<Venta> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Buscar por tipo de pago
    Page<Venta> findByTipoPago(String tipoPago, Pageable pageable);

    // Buscar por rango de fechas
    Page<Venta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);

    // Buscar por rango de totales
    Page<Venta> findByTotalBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    // Ventas del día
    List<Venta> findByFecha(LocalDate fecha);

    // NUEVO: Ventas por cliente en un rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.cliente.cedula = :cedula AND v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findVentasByClienteAndFechas(
            @Param("cedula") Integer cedula,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // NUEVO: Total de ventas en un rango de fechas
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calcularTotalVentasPorRango(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // NUEVO: Contar ventas por tipo de pago
    @Query("SELECT v.tipoPago, COUNT(v) FROM Venta v GROUP BY v.tipoPago")
    List<Object[]> contarVentasPorTipoPago();
}