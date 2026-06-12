package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recursos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String tipo; // PDF, VIDEO, DOCUMENTO, LINK

    @Column(nullable = false)
    private String url;

    private String tamano; // Ej: "2.4 MB"

    private LocalDateTime fechaSubida;
}
