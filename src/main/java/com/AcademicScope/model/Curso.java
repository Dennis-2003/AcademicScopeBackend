package com.AcademicScope.model;

import com.AcademicScope.enums.TipoCurso;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "cursos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(unique = true)
    private String codigo;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TipoCurso tipo = TipoCurso.REGULAR;

    @ManyToOne
    @JoinColumn(name = "grado_id")
    private Grado grado;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Usuario docente;

    public int getMatriculados() {
        return grado != null && grado.getMatriculas() != null ? grado.getMatriculas().size() : 0;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private List<Evaluacion> evaluaciones;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDate.now();
        }
    }
}
