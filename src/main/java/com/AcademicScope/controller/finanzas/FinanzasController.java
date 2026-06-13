package com.AcademicScope.controller.finanzas;

import com.AcademicScope.model.ConceptoPago;
import com.AcademicScope.model.PagoEstudiante;
import com.AcademicScope.service.finanzas.FinanzasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas")
@RequiredArgsConstructor
public class FinanzasController {

    private final FinanzasService finanzasService;

    @GetMapping("/conceptos")
    public ResponseEntity<List<ConceptoPago>> listarConceptos() {
        return ResponseEntity.ok(finanzasService.listarConceptos());
    }

    @PostMapping("/conceptos")
    public ResponseEntity<ConceptoPago> crearConcepto(@RequestBody ConceptoPago concepto) {
        return ResponseEntity.ok(finanzasService.crearConcepto(concepto));
    }

    @GetMapping("/pagos")
    public ResponseEntity<List<PagoEstudiante>> listarPagos() {
        return ResponseEntity.ok(finanzasService.listarPagos());
    }

    @GetMapping("/pagos/estudiante/{id}")
    public ResponseEntity<List<PagoEstudiante>> listarPagosEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(finanzasService.listarPagosPorEstudiante(id));
    }

    @PostMapping("/pagos/toggle/{estudianteId}/{conceptoId}")
    public ResponseEntity<PagoEstudiante> togglePago(
            @PathVariable Long estudianteId,
            @PathVariable Long conceptoId) {
        return ResponseEntity.ok(finanzasService.togglePago(estudianteId, conceptoId));
    }
}
