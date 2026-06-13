package com.AcademicScope.service.asignacion;

import com.AcademicScope.repository.asignacion.EntregaAsignacionRepository;
import com.AcademicScope.model.EntregaAsignacion;
import com.AcademicScope.repository.asignacion.AsignacionRepository;
import com.AcademicScope.model.Asignacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final EntregaAsignacionRepository entregaRepository;

    public Asignacion crear(Asignacion asignacion) {
        asignacion.setFechaRegistro(LocalDateTime.now());
        if(asignacion.getEstado() == null) asignacion.setEstado("ACTIVA");
        return asignacionRepository.save(asignacion);
    }

    public List<Asignacion> listarPorCurso(Long cursoId) {
        return asignacionRepository.findByCursoId(cursoId);
    }

    public void eliminar(Long id) {
        asignacionRepository.deleteById(id);
    }

    public Asignacion obtenerPorId(Long id) {
        return asignacionRepository.findById(id).orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
    }
    
    public List<EntregaAsignacion> obtenerEntregas(Long asignacionId) {
        return entregaRepository.findByAsignacionId(asignacionId);
    }

    public List<Asignacion> listarPorEstudiante(Long estudianteId) {
        return asignacionRepository.findByEstudianteId(estudianteId);
    }
}
