package com.ferregestion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credito")
    private Integer idCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    // ✅ CAMBIADO: Ahora mapea a "cedula"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cedula", nullable = false, referencedColumnName = "cedula")
    private Cliente cliente;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "saldo_pendiente", nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoPendiente;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
}