package com.ferregestion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {
    private Integer idProducto;
    private String descripcion;
    private GrupoResponseDTO grupo;
    private BigDecimal iva;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stock;
}