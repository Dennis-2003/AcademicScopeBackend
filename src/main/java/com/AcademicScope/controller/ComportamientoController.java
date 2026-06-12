package com.AcademicScope.controller;

import com.AcademicScope.model.Comportamiento;
import com.AcademicScope.service.ComportamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comportamientos")
@RequiredArgsConstructor
public class ComportamientoController {

    private final ComportamientoService comportamientoService;

    @PostMapping
    public ResponseEntity<Comportamiento> registrar(@RequestBody Comportamiento comportamiento) {
        return new ResponseEntity<>(comportamientoService.registrar(comportamiento), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comportamiento> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(comportamientoService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{estudianteId}/periodo/{periodoId}")
    public ResponseEntity<List<Comportamiento>> listarPorEstudianteYPeriodo(
            @PathVariable Long estudianteId, @PathVariable Long periodoId) {
        return ResponseEntity.ok(comportamientoService.listarPorEstudianteYPeriodo(estudianteId, periodoId));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Comportamiento>> listarPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(comportamientoService.listarPorEstudiante(estudianteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comportamientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
