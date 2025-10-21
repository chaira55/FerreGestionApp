package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_cotizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_cotizacion")  // CAMBIO: id_detalle → id_detalle_cotizacion
    private Integer idDetalleCotizacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

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

    @NotNull(message = "El precio unitario es obligatorio")
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private Double precioUnitario;

    @NotNull(message = "El subtotal es obligatorio")
    @Column(name = "subtotal_producto", nullable = false, precision = 12, scale = 2)  // CAMBIO: subtotal → subtotal_producto
    private Double subtotalProducto;
}