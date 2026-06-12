package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaVencimiento;

    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private String estado; // ACTIVA, COMPLETADA
}
