package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos_estudiante")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagoEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concepto_id", nullable = false)
    private ConceptoPago concepto;

    @Column(nullable = false)
    @Builder.Default
    private Boolean pagado = false;

    private LocalDateTime fechaPago;
}
