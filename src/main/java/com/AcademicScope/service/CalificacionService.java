package com.AcademicScope.service;

import com.AcademicScope.model.Calificacion;
import com.AcademicScope.enums.ColorPerformance;
import com.AcademicScope.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;

    public Calificacion calificar(Calificacion calificacion) {
        return calificacionRepository.save(calificacion);
    }

    public Calificacion actualizar(Long id, Calificacion calificacion) {
        return calificacionRepository.findById(id).map(c -> {
            c.setNota(calificacion.getNota());
            c.setComentarioDocente(calificacion.getComentarioDocente());
            c.setRecomendacion(calificacion.getRecomendacion());
            c.setAspectoConducta(calificacion.getAspectoConducta());
            return calificacionRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Calificacion no encontrada"));
    }

    public Calificacion obtenerPorId(Long id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificacion no encontrada"));
    }

    public List<Calificacion> listarPorEvaluacion(Long evaluacionId) {
        return calificacionRepository.findByEvaluacionId(evaluacionId);
    }

    public List<Calificacion> listarPorEstudiante(Long estudianteId) {
        return calificacionRepository.findByEstudianteId(estudianteId);
    }

    public void eliminar(Long id) {
        calificacionRepository.deleteById(id);
    }

    public ColorPerformance calcularColor(Double nota) {
        if (nota == null) return ColorPerformance.GRIS;
        if (nota >= 18) return ColorPerformance.AZUL;
        if (nota >= 14) return ColorPerformance.VERDE;
        if (nota >= 11) return ColorPerformance.AMARILLO;
        return ColorPerformance.ROJO;
    }
}
