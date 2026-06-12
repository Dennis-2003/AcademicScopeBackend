package com.AcademicScope.controller;

import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
        return new ResponseEntity<>(cursoService.crear(curso), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.actualizar(id, curso));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    @GetMapping("/grado/{gradoId}")
    public ResponseEntity<List<Curso>> listarPorGrado(@PathVariable Long gradoId) {
        return ResponseEntity.ok(cursoService.listarPorGrado(gradoId));
    }

    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<Curso>> listarPorDocente(@PathVariable Long docenteId) {
        return ResponseEntity.ok(cursoService.listarPorDocente(docenteId));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Curso>> listarPorTipo(@PathVariable TipoCurso tipo) {
        return ResponseEntity.ok(cursoService.listarPorTipo(tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
