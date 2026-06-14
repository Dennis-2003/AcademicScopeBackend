package com.AcademicScope.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenHijoDTO {
    private Long id;
    private String nombre;
    private String grado;
    private String semaforo; // VERDE, AMBAR, ROJO, GRIS
    private int asistencia;
}
