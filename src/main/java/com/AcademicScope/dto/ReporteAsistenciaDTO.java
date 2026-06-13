package com.AcademicScope.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReporteAsistenciaDTO {
    private long asistencia;
    private long ausentes;
    private long tardanzas;
    private long justificados;

    private List<CursoRiesgoDTO> cursosRiesgo;
    private List<EstudianteRiesgoDTO> estudiantesRiesgo;

    @Data
    @Builder
    public static class CursoRiesgoDTO {
        private String curso;
        private String grado;
        private long ausencias;
        private String docente;
    }

    @Data
    @Builder
    public static class EstudianteRiesgoDTO {
        private String nombre;
        private String grado;
        private long faltas;
    }
}
