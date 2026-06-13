package com.AcademicScope.auth.dto;

import com.AcademicScope.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String dni;
    private RolUsuario rol;
    private boolean primerIngreso;
}
