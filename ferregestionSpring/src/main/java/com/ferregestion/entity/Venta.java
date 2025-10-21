package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cedula", nullable = false)
    private Cliente cliente;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;  // NUEVO: Relación opcional con cotización

    @NotNull(message = "La fecha de venta es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;  // CAMBIO: LocalDateTime → LocalDate

    @NotNull(message = "El total es obligatorio")
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private Double total;

    @NotNull(message = "El tipo de pago es obligatorio")
    @Column(name = "tipo_pago", nullable = false, length = 20)
    private String tipoPago;  // NUEVO: Campo obligatorio

    // Relación con DetalleVenta
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    // NUEVO: Relación bidireccional con Credito (opcional)
    @OneToOne(mappedBy = "venta", cascade = CascadeType.ALL)
    private Credito credito;
}