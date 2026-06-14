package com.AcademicScope.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaDTO {
    private String id;
    private String tipo; // ROJO, AMBAR
    private String titulo;
    private String mensaje;
}
