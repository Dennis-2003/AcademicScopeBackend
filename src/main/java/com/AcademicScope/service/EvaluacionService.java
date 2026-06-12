package com.AcademicScope.service;

import com.AcademicScope.model.Evaluacion;
import com.AcademicScope.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    public Evaluacion crear(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public Evaluacion actualizar(Long id, Evaluacion evaluacion) {
        return evaluacionRepository.findById(id).map(e -> {
            e.setNombre(evaluacion.getNombre());
            e.setDescripcion(evaluacion.getDescripcion());
            e.setPonderacion(evaluacion.getPonderacion());
            e.setOrden(evaluacion.getOrden());
            return evaluacionRepository.save(e);
        }).orElseThrow(() -> new RuntimeException("Evaluacion no encontrada"));
    }

    public Evaluacion obtenerPorId(Long id) {
        return evaluacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluacion no encontrada"));
    }

    public List<Evaluacion> listarPorCursoYPeriodo(Long cursoId, Long periodoId) {
        return evaluacionRepository.findByCursoIdAndPeriodoId(cursoId, periodoId);
    }

    public List<Evaluacion> listarPorCurso(Long cursoId) {
        return evaluacionRepository.findByCursoId(cursoId);
    }

    public void eliminar(Long id) {
        evaluacionRepository.deleteById(id);
    }
}
