package com.ferregestion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CotizacionResponseDTO {

    private Integer idCotizacion;
    private ClienteResponseDTO cliente;
    private String nombre;
    private LocalDate fecha;
    private BigDecimal total;
    private List<DetalleCotizacionResponseDTO> detalles;
}