package com.ferregestion.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class VentaRequestDTO {

    @NotNull(message = "La cédula del cliente es obligatoria")
    private Integer cedula;

    @Size(max = 100)
    private String nombre;

    private Integer idCotizacion;  // Opcional

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El total es obligatorio")
    private BigDecimal total;

    @NotBlank(message = "El tipo de pago es obligatorio")
    private String tipoPago;

    @Valid
    @NotNull(message = "Los detalles son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un detalle")
    private List<DetalleVentaRequestDTO> detalles;
}