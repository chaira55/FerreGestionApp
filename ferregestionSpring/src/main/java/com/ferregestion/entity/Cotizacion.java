package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cotizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Integer idCotizacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cedula", nullable = false)
    private Cliente cliente;

    @Column(name = "nombre", length = 100)  // NUEVO
    private String nombre;

    @NotNull(message = "La fecha de cotización es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El total de la cotización es obligatorio")
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private Double total;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles;
}