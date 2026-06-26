package com.AcademicScope.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@SuppressWarnings("all")
public class LoginRequest {
    @NotBlank(message = "El email/DNI no puede estar vacío")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
