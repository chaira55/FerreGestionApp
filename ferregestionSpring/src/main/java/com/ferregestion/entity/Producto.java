package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private String descripcion;  // CAMBIO: nombre → descripcion

    @ManyToOne
    @JoinColumn(name = "codigo_clase")
    private Clase clase;  // NUEVO: Relación con Clase (opcional)

    @ManyToOne
    @JoinColumn(name = "codigo_grupo")
    private Grupo grupo;  // NUEVO: Relación con Grupo (opcional)

    @Column(name = "iva", precision = 5, scale = 2, columnDefinition = "DECIMAL(5,2) DEFAULT 19.00")
    private Double iva = 19.00;  // NUEVO

    @Column(name = "precio_compra", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private Double precioCompra = 0.00;  // NUEVO

    @Column(name = "precio_venta", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private Double precioVenta = 0.00;  // CAMBIO: precio → precioVenta

    @Column(name = "stock", columnDefinition = "INT DEFAULT 0")
    private Integer stock = 0;

    // Relaciones inversas (opcional)
    @OneToMany(mappedBy = "producto")
    private List<DetalleVenta> detallesVenta;

    @OneToMany(mappedBy = "producto")
    private List<DetalleCotizacion> detallesCotizacion;
}