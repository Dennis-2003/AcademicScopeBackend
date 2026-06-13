package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Destinatario

    @ManyToOne
    @JoinColumn(name = "remitente_id", nullable = true)
    private Usuario remitente; // Quien envía

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String mensaje;

    @Builder.Default
    private Boolean leido = false;

    private LocalDateTime fechaEnvio;
}
