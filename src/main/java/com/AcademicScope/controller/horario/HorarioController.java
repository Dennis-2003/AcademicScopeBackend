package com.AcademicScope.controller.horario;

import com.AcademicScope.service.horario.HorarioService;
import com.AcademicScope.model.Horario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @GetMapping
    public ResponseEntity<List<Horario>> listarTodos() {
        return ResponseEntity.ok(horarioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Horario> crear(@RequestBody Horario horario) {
        return new ResponseEntity<>(horarioService.crear(horario), HttpStatus.CREATED);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Horario>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(horarioService.listarPorCurso(cursoId));
    }

    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<Horario>> listarPorDocente(@PathVariable Long docenteId) {
        return ResponseEntity.ok(horarioService.listarPorDocente(docenteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizar(@PathVariable Long id, @RequestBody Horario horario) {
        return ResponseEntity.ok(horarioService.actualizar(id, horario));
    }
}
