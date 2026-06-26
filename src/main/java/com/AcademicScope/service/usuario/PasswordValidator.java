package com.AcademicScope.service.usuario;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("all")
public class PasswordValidator {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@!._\\-])[A-Za-z\\d@!._\\-]{8,}$"
    );

    public static void validate(String password) {
        if (password == null || password.isBlank()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }
        if (password.contains("#") || password.contains("$")) {
            throw new RuntimeException("La contraseña no puede contener los caracteres # ni $");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new RuntimeException(
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@ ! . _ -)"
            );
        }
    }
}

