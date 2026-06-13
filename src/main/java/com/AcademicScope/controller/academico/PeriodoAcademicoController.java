package com.AcademicScope.controller.academico;

import com.AcademicScope.service.academico.PeriodoAcademicoService;
import com.AcademicScope.model.PeriodoAcademico;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
@RequiredArgsConstructor
public class PeriodoAcademicoController {

    private final PeriodoAcademicoService periodoService;

    @PostMapping
    public ResponseEntity<PeriodoAcademico> crear(@RequestBody PeriodoAcademico periodo) {
        return new ResponseEntity<>(periodoService.crear(periodo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodoAcademico> actualizar(@PathVariable Long id, @RequestBody PeriodoAcademico periodo) {
        return ResponseEntity.ok(periodoService.actualizar(id, periodo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoAcademico> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(periodoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PeriodoAcademico>> listar() {
        return ResponseEntity.ok(periodoService.listarTodos());
    }

    @GetMapping("/activo")
    public ResponseEntity<PeriodoAcademico> obtenerActivo() {
        return ResponseEntity.ok(periodoService.obtenerActivo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        periodoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
