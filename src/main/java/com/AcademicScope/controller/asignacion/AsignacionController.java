package com.AcademicScope.controller.asignacion;

import com.AcademicScope.model.EntregaAsignacion;
import com.AcademicScope.service.asignacion.AsignacionService;
import com.AcademicScope.model.Asignacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @PostMapping
    public ResponseEntity<Asignacion> crear(@RequestBody Asignacion asignacion) {
        return new ResponseEntity<>(asignacionService.crear(asignacion), HttpStatus.CREATED);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Asignacion>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(asignacionService.listarPorCurso(cursoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(asignacionService.obtenerPorId(id));
    }

    @GetMapping("/{id}/entregas")
    public ResponseEntity<List<EntregaAsignacion>> listarEntregas(@PathVariable Long id) {
        return ResponseEntity.ok(asignacionService.obtenerEntregas(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
