package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "comunicados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false, length = 50)
    private String audiencia; // TODOS, ESTUDIANTES, DOCENTES, APODERADOS

    @Column(nullable = false, length = 50)
    private String prioridad; // ALTA, MEDIA, BAJA

    @Column(nullable = false)
    private LocalDate fecha; // "2026-06-05"

    @Column(length = 255)
    private String archivoUrl; // Ruta o URL del archivo adjunto

    @Column(length = 255)
    private String archivoNombre; // Nombre original del archivo adjunto
}
