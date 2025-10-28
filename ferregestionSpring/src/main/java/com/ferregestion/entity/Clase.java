package com.ferregestion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "codigo_clase", length = 20)
    private String codigoClase;  // CAMBIO: Esta es la PK real

    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    // Relación con Producto (no con Grupo)
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Producto> productos;  // CAMBIO: grupos → productos
}