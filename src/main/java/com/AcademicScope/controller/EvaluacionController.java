package com.AcademicScope.controller;

import com.AcademicScope.model.Evaluacion;
import com.AcademicScope.service.EvaluacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<Evaluacion> crear(@RequestBody Evaluacion evaluacion) {
        return new ResponseEntity<>(evaluacionService.crear(evaluacion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizar(@PathVariable Long id, @RequestBody Evaluacion evaluacion) {
        return ResponseEntity.ok(evaluacionService.actualizar(id, evaluacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.obtenerPorId(id));
    }

    @GetMapping("/curso/{cursoId}/periodo/{periodoId}")
    public ResponseEntity<List<Evaluacion>> listarPorCursoYPeriodo(@PathVariable Long cursoId, @PathVariable Long periodoId) {
        return ResponseEntity.ok(evaluacionService.listarPorCursoYPeriodo(cursoId, periodoId));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Evaluacion>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(evaluacionService.listarPorCurso(cursoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
