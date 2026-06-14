package com.AcademicScope.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorDashboardDTO {
    private int hijos;
    private int notificaciones;
    private List<ResumenHijoDTO> resumenHijos;
    private List<AlertaDTO> alertas;
}
