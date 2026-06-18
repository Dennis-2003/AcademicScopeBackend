package com.AcademicScope.controller.recurso;

import com.AcademicScope.service.recurso.RecursoService;
import com.AcademicScope.model.Recurso;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
public class RecursoController {

    private final RecursoService recursoService;

    @PostMapping
    public ResponseEntity<Recurso> crear(@RequestBody Recurso recurso) {
        return new ResponseEntity<>(recursoService.crear(recurso), HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<Recurso> subir(
            @RequestParam("file") MultipartFile file,
            @RequestParam("curso") Long cursoId,
            @RequestParam("titulo") String titulo,
            @RequestParam("tipo") String tipo) {
        return new ResponseEntity<>(recursoService.subirRecurso(file, cursoId, titulo, tipo), HttpStatus.CREATED);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Recurso>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(recursoService.listarPorCurso(cursoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recurso> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(recursoService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
