package com.ferregestion.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CotizacionRequestDTO {

    @NotNull(message = "La cédula del cliente es obligatoria")
    private Integer cedula;

    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El total es obligatorio")
    private BigDecimal total;

    @Valid
    @NotNull(message = "Los detalles son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un detalle")
    private List<DetalleCotizacionRequestDTO> detalles;
}