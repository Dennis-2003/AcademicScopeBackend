package com.AcademicScope.model;

import com.AcademicScope.enums.EstadoMatricula;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    private PeriodoAcademico periodo;

    @Enumerated(EnumType.STRING)
    private EstadoMatricula estado;

    private LocalDate fechaMatricula;
}
