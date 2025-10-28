package com.ferregestion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDTO {
    private Integer idPago;
    private Integer idCredito;
    private ClienteResponseDTO cliente;
    private String nombre;
    private LocalDate fechaPago;
    private BigDecimal monto;
}