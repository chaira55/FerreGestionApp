package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClase;

    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    @Column(name = "descripcion", length = 250)
    private String descripcion;

    // Relaciones con Grupo
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grupo> grupos;
}
