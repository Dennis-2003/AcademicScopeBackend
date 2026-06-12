package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "calificaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @Column(nullable = false)
    private Double nota;

    private String comentarioDocente;

    private String recomendacion;

    private String aspectoConducta;

    private LocalDate fechaRegistro;
}
