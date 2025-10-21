package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")  // CAMBIO: id_detalle → id_detalle_venta
    private Integer idDetalleVenta;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "descripcion_producto", length = 200)  // NUEVO: para histórico
    private String descripcionProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(name = "cantidad", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer cantidad = 1;

    @NotNull(message = "El precio unitario es obligatorio")  // NUEVO
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private Double precioUnitario;

    @NotNull(message = "El subtotal es obligatorio")
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private Double subtotal;
}