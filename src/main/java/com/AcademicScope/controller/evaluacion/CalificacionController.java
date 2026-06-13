package com.AcademicScope.controller.evaluacion;

import com.AcademicScope.service.evaluacion.CalificacionService;
import com.AcademicScope.model.Calificacion;
import com.AcademicScope.enums.ColorPerformance;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PostMapping
    public ResponseEntity<Calificacion> calificar(@RequestBody Calificacion calificacion) {
        return new ResponseEntity<>(calificacionService.calificar(calificacion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizar(@PathVariable Long id, @RequestBody Calificacion calificacion) {
        return ResponseEntity.ok(calificacionService.actualizar(id, calificacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.obtenerPorId(id));
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<List<Calificacion>> listarPorEvaluacion(@PathVariable Long evaluacionId) {
        return ResponseEntity.ok(calificacionService.listarPorEvaluacion(evaluacionId));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Calificacion>> listarPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(calificacionService.listarPorEstudiante(estudianteId));
    }

    @PostMapping("/calcular-color")
    public ResponseEntity<Map<String, ColorPerformance>> calcularColor(@RequestBody Map<String, Double> body) {
        ColorPerformance color = calificacionService.calcularColor(body.get("nota"));
        return ResponseEntity.ok(Map.of("color", color));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        calificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
