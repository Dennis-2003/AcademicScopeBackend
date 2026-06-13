package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conceptos_pago")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConceptoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Double monto;
}
