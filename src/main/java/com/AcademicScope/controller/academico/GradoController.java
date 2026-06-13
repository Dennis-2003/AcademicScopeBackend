package com.AcademicScope.controller.academico;

import com.AcademicScope.service.academico.GradoService;
import com.AcademicScope.model.Grado;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grados")
@RequiredArgsConstructor
public class GradoController {

    private final GradoService gradoService;

    @PostMapping
    public ResponseEntity<Grado> crear(@RequestBody Grado grado) {
        return new ResponseEntity<>(gradoService.crear(grado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grado> actualizar(@PathVariable Long id, @RequestBody Grado grado) {
        return ResponseEntity.ok(gradoService.actualizar(id, grado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grado> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(gradoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Grado>> listar() {
        return ResponseEntity.ok(gradoService.listarTodos());
    }

    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<Grado>> listarPorNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(gradoService.listarPorNivel(nivel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gradoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
