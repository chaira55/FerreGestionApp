package com.ferregestion.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditoRequestDTO {

    @NotNull(message = "El ID de venta es obligatorio")
    private Integer idVenta;

    @NotNull(message = "La cédula es obligatoria")
    private Integer cedula;

    private String nombre;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto debe ser mayor a 0")
    private BigDecimal montoTotal;

    @NotNull(message = "El saldo pendiente es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo debe ser mayor o igual a 0")
    private BigDecimal saldoPendiente;

    private String estado;
}