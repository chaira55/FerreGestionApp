package com.ferregestion.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    private String codigoGrupo;

    @DecimalMin(value = "0.0", message = "El IVA debe ser mayor o igual a 0")
    private BigDecimal iva;

    @DecimalMin(value = "0.0", message = "El precio de compra debe ser mayor o igual a 0")
    private BigDecimal precioCompra;

    @DecimalMin(value = "0.0", message = "El precio de venta debe ser mayor o igual a 0")
    private BigDecimal precioVenta;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}