package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cedula_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El crédito es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_credito", nullable = false)
    private Credito credito;

    @NotNull(message = "El valor es obligatorio")
    @Column(name = "valor", nullable = false)
    private Double valor;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado", length = 20)
    private String estado; // ej: PENDIENTE, COMPLETADO, ANULADO
}
