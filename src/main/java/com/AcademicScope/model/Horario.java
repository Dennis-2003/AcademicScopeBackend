package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "horarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private String diaSemana; // LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO

    @Column(nullable = false)
    private String horaInicio; // Formato "HH:mm"

    @Column(nullable = false)
    private String horaFin; // Formato "HH:mm"

    private String aula; // Ejemplo: "Laboratorio A", "Pabellón 2"
}
