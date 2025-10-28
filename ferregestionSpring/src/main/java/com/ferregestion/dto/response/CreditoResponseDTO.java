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
public class CreditoResponseDTO {
    private Integer idCredito;
    private Integer idVenta;
    private ClienteResponseDTO cliente;
    private String nombre;
    private BigDecimal montoTotal;
    private BigDecimal saldoPendiente;
    private String estado;
}