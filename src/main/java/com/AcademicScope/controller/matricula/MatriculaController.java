package com.AcademicScope.controller.matricula;

import com.AcademicScope.service.matricula.MatriculaService;
import com.AcademicScope.model.Matricula;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<Matricula> matricular(@RequestBody Matricula matricula) {
        return new ResponseEntity<>(matriculaService.matricular(matricula), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/retirar")
    public ResponseEntity<Matricula> retirar(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.retirar(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matricula> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Matricula>> listarPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(matriculaService.listarPorEstudiante(estudianteId));
    }

    @GetMapping("/grado/{gradoId}")
    public ResponseEntity<List<Matricula>> listarPorGrado(@PathVariable Long gradoId) {
        return ResponseEntity.ok(matriculaService.listarPorGrado(gradoId));
    }

    @GetMapping("/periodo/{periodoId}")
    public ResponseEntity<List<Matricula>> listarPorPeriodo(@PathVariable Long periodoId) {
        return ResponseEntity.ok(matriculaService.listarPorPeriodo(periodoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        matriculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
