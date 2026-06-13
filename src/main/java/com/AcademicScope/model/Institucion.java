package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "instituciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreColegio;

    @Column(unique = true, nullable = false)
    private String ruc;

    @Column(columnDefinition = "LONGTEXT")
    private String logoUrl;

    @Column(nullable = false)
    @Builder.Default
    private String planSuscripcion = "FREE";

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
