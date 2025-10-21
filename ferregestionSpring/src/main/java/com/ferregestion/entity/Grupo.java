package com.ferregestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "grupo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {

    @Id
    @Column(name = "codigo_grupo", length = 20)
    private String codigoGrupo;  // CAMBIO: PK es String, no Integer autoincremental

    @NotBlank(message = "El nombre del grupo es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "iva", precision = 5, scale = 2, columnDefinition = "DECIMAL(5,2) DEFAULT 19.00")
    private Double iva = 19.00;  // NUEVO: Campo obligatorio con default

    // ELIMINADO: descripcion (no existe en la BD)
    // ELIMINADO: relación con Clase (no existe en la BD)

    // Relación con Producto
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Producto> productos;
}