package com.AcademicScope.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "grados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String nivel;

    @JsonIgnore
    @OneToMany(mappedBy = "grado")
    private List<Curso> cursos;
}
