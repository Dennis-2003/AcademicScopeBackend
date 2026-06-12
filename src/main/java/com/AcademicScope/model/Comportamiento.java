package com.AcademicScope.model;

import com.AcademicScope.enums.TipoConducta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "comportamientos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comportamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    private PeriodoAcademico periodo;

    @ManyToOne
    @JoinColumn(name = "docente_id", nullable = false)
    private Usuario docente;

    @Enumerated(EnumType.STRING)
    private TipoConducta tipo;

    @Column(length = 500)
    private String descripcion;

    private LocalDate fecha;
}
