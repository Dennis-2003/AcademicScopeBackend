package com.AcademicScope.controller.asignacion;

import com.AcademicScope.model.EntregaAsignacion;
import com.AcademicScope.repository.asignacion.EntregaAsignacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
public class EntregaAsignacionController {

    private final EntregaAsignacionRepository entregaRepository;

    @PostMapping
    public ResponseEntity<EntregaAsignacion> crearEntrega(@RequestBody EntregaAsignacion entrega) {
        entrega.setFechaEntrega(LocalDateTime.now());
        entrega.setEstado("ENTREGADO");
        return new ResponseEntity<>(entregaRepository.save(entrega), HttpStatus.CREATED);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<EntregaAsignacion>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(entregaRepository.findByEstudianteId(estudianteId));
    }

    @PutMapping("/{id}/revisar")
    public ResponseEntity<EntregaAsignacion> revisarEntrega(@PathVariable Long id, @RequestBody EntregaAsignacion datosRevisados) {
        EntregaAsignacion entrega = entregaRepository.findById(id).orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        entrega.setCumplio(datosRevisados.getCumplio());
        entrega.setRetroalimentacion(datosRevisados.getRetroalimentacion());
        entrega.setEstado("REVISADO");
        return ResponseEntity.ok(entregaRepository.save(entrega));
    }
}
