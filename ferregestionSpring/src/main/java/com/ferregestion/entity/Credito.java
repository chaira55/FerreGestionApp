package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCredito;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cedula_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El valor es obligatorio")
    @Column(name = "valor", nullable = false)
    private Double valor;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "estado", length = 20)
    private String estado; // ej: ACTIVO, PAGADO, VENCIDO
}
