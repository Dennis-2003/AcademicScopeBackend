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
public class DashboardRendimientoDTO {
    private List<String> meses;
    private List<Integer> cursosActivos;
    private List<Integer> asistenciaMedia;
}
