package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodos_academicos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Builder.Default
    private Boolean activo = false;
}
