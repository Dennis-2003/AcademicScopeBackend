package com.AcademicScope.service.comportamiento;

import com.AcademicScope.repository.comportamiento.ComportamientoRepository;
import com.AcademicScope.model.Comportamiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class ComportamientoService {

    private final ComportamientoRepository comportamientoRepository;

    public Comportamiento registrar(Comportamiento comportamiento) {
        comportamiento.setFecha(LocalDate.now());
        return comportamientoRepository.save(comportamiento);
    }

    public List<Comportamiento> listarTodos() {
        return comportamientoRepository.findAllByOrderByFechaDescIdDesc();
    }

    public Comportamiento obtenerPorId(Long id) {
        return comportamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comportamiento no encontrado"));
    }

    public List<Comportamiento> listarPorEstudianteYPeriodo(Long estudianteId, Long periodoId) {
        return comportamientoRepository.findByEstudianteIdAndPeriodoId(estudianteId, periodoId);
    }

    public List<Comportamiento> listarPorEstudiante(Long estudianteId) {
        return comportamientoRepository.findByEstudianteId(estudianteId);
    }

    public void eliminar(Long id) {
        comportamientoRepository.deleteById(id);
    }
}

