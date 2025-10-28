package com.ferregestion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "codigo_clase")
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "codigo_grupo")
    private Grupo grupo;

    @Column(name = "iva", precision = 5, scale = 2)
    private BigDecimal iva = new BigDecimal("19.00");

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra = BigDecimal.ZERO;

    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta = BigDecimal.ZERO;

    @Column(name = "stock")
    private Integer stock = 0;

    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<DetalleVenta> detallesVenta;

    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<DetalleCotizacion> detallesCotizacion;
}