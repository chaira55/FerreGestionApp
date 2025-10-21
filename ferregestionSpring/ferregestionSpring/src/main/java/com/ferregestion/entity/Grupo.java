package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "grupo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGrupo;

    @NotBlank(message = "El nombre del grupo es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    @Column(name = "descripcion", length = 250)
    private String descripcion;

    // Relación con Clase
    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;
}
