package com.AcademicScope.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CambioPasswordDTO {
    private String dni;
    private String passwordActual;
    private String passwordNuevo;
}
