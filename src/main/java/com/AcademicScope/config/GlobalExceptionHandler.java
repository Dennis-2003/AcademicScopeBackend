package com.AcademicScope.config;

import com.AcademicScope.dto.ErrorViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorViewModel> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
        ErrorViewModel error = ErrorViewModel.builder()
                .codigo(HttpStatus.UNAUTHORIZED.value())
                .mensaje("Credenciales incorrectas")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorViewModel> handleRuntime(RuntimeException ex) {
        ErrorViewModel error = ErrorViewModel.builder()
                .codigo(HttpStatus.BAD_REQUEST.value())
                .mensaje(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorViewModel> handleUserNotFound(UsernameNotFoundException ex) {
        ErrorViewModel error = ErrorViewModel.builder()
                .codigo(HttpStatus.UNAUTHORIZED.value())
                .mensaje(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorViewModel> handleAccessDenied(AccessDeniedException ex) {
        ErrorViewModel error = ErrorViewModel.builder()
                .codigo(HttpStatus.FORBIDDEN.value())
                .mensaje("No tienes permisos para acceder a este recurso")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorViewModel> handleGeneral(Exception ex) {
        ErrorViewModel error = ErrorViewModel.builder()
                .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .mensaje("Error interno del servidor")
                .detalles(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
