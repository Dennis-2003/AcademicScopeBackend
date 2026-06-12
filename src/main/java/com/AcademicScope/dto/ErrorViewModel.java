package com.AcademicScope.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorViewModel {

    private int codigo;
    private String mensaje;
    private String detalles;
    private LocalDateTime timestamp;
}
