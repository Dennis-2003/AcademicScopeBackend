package com.AcademicScope.service.finanzas;

import com.AcademicScope.model.ConceptoPago;
import com.AcademicScope.model.PagoEstudiante;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.repository.finanzas.ConceptoPagoRepository;
import com.AcademicScope.repository.finanzas.PagoEstudianteRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinanzasService {

    private final ConceptoPagoRepository conceptoPagoRepository;
    private final PagoEstudianteRepository pagoEstudianteRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ConceptoPago> listarConceptos() {
        return conceptoPagoRepository.findAll();
    }

    @Transactional
    public ConceptoPago crearConcepto(ConceptoPago concepto) {
        return conceptoPagoRepository.save(concepto);
    }

    public List<PagoEstudiante> listarPagos() {
        return pagoEstudianteRepository.findAll();
    }

    public List<PagoEstudiante> listarPagosPorEstudiante(Long estudianteId) {
        return pagoEstudianteRepository.findByEstudianteId(estudianteId);
    }

    @Transactional
    public PagoEstudiante togglePago(Long estudianteId, Long conceptoId) {
        Optional<PagoEstudiante> pagoOpt = pagoEstudianteRepository.findByEstudianteIdAndConceptoId(estudianteId, conceptoId);
        
        if (pagoOpt.isPresent()) {
            PagoEstudiante pago = pagoOpt.get();
            pago.setPagado(!pago.getPagado());
            if (pago.getPagado()) {
                pago.setFechaPago(LocalDateTime.now());
            } else {
                pago.setFechaPago(null);
            }
            return pagoEstudianteRepository.save(pago);
        } else {
            Usuario estudiante = usuarioRepository.findById(estudianteId)
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
            ConceptoPago concepto = conceptoPagoRepository.findById(conceptoId)
                    .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));

            PagoEstudiante nuevoPago = PagoEstudiante.builder()
                    .estudiante(estudiante)
                    .concepto(concepto)
                    .pagado(true)
                    .fechaPago(LocalDateTime.now())
                    .build();

            return pagoEstudianteRepository.save(nuevoPago);
        }
    }
}
