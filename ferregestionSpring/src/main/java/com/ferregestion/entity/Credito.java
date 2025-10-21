package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credito")
    private Integer idCredito;

    @NotNull
    @OneToOne  // CAMBIO: De Integer a relación OneToOne
    @JoinColumn(name = "id_venta", nullable = false, unique = true)
    private Venta venta;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cedula", nullable = false)
    private Cliente cliente;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private Double montoTotal;

    @Column(name = "saldo_pendiente", nullable = false, precision = 12, scale = 2)
    private Double saldoPendiente;

    @Column(name = "estado", length = 50)
    private String estado;
}