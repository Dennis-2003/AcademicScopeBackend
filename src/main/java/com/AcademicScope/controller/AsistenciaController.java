package com.AcademicScope.controller;

import com.AcademicScope.model.Asistencia;
import com.AcademicScope.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<Asistencia> registrar(@RequestBody Asistencia asistencia) {
        return new ResponseEntity<>(asistenciaService.registrar(asistencia), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{estudianteId}/curso/{cursoId}")
    public ResponseEntity<List<Asistencia>> listarPorEstudianteYCurso(
            @PathVariable Long estudianteId, @PathVariable Long cursoId) {
        return ResponseEntity.ok(asistenciaService.listarPorEstudianteYCurso(estudianteId, cursoId));
    }

    @GetMapping("/curso/{cursoId}/fecha/{fecha}")
    public ResponseEntity<List<Asistencia>> listarPorCursoYFecha(
            @PathVariable Long cursoId, @PathVariable String fecha) {
        return ResponseEntity.ok(asistenciaService.listarPorCursoYFecha(cursoId, LocalDate.parse(fecha)));
    }

    @GetMapping("/faltas/{estudianteId}/curso/{cursoId}")
    public ResponseEntity<Long> contarFaltas(@PathVariable Long estudianteId, @PathVariable Long cursoId) {
        return ResponseEntity.ok(asistenciaService.contarFaltas(estudianteId, cursoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
