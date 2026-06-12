package com.AcademicScope.service;

import com.AcademicScope.model.Recurso;
import com.AcademicScope.repository.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoService {

    private final RecursoRepository recursoRepository;

    public Recurso crear(Recurso recurso) {
        recurso.setFechaSubida(LocalDateTime.now());
        return recursoRepository.save(recurso);
    }

    public List<Recurso> listarPorCurso(Long cursoId) {
        return recursoRepository.findByCursoId(cursoId);
    }

    public void eliminar(Long id) {
        recursoRepository.deleteById(id);
    }

    public Recurso obtenerPorId(Long id) {
        return recursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
    }
}
