package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "evaluaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    private PeriodoAcademico periodo;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    private LocalDate fecha;

    private Double ponderacion;

    private Integer orden;
}
