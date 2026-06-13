package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entregas_asignacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntregaAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asignacion_id", nullable = false)
    private Asignacion asignacion;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    private LocalDateTime fechaEntrega;

    @Column(nullable = false)
    private String estado; // ENTREGADO, ATRASADO

    @Column(columnDefinition = "TEXT")
    private String archivoUrl;

    private Boolean cumplio;

    @Column(columnDefinition = "TEXT")
    private String retroalimentacion;
}
