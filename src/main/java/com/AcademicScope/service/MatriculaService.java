package com.AcademicScope.service;

import com.AcademicScope.model.Matricula;
import com.AcademicScope.enums.EstadoMatricula;
import com.AcademicScope.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;

    public Matricula matricular(Matricula matricula) {
        matricula.setEstado(EstadoMatricula.ACTIVA);
        matricula.setFechaMatricula(LocalDate.now());
        return matriculaRepository.save(matricula);
    }

    public Matricula retirar(Long id) {
        return matriculaRepository.findById(id).map(m -> {
            m.setEstado(EstadoMatricula.RETIRADA);
            return matriculaRepository.save(m);
        }).orElseThrow(() -> new RuntimeException("Matricula no encontrada"));
    }

    public Matricula obtenerPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matricula no encontrada"));
    }

    public List<Matricula> listarPorEstudiante(Long estudianteId) {
        return matriculaRepository.findByEstudianteId(estudianteId);
    }

    public List<Matricula> listarPorCurso(Long cursoId) {
        return matriculaRepository.findByCursoId(cursoId);
    }

    public List<Matricula> listarPorPeriodo(Long periodoId) {
        return matriculaRepository.findByPeriodoId(periodoId);
    }

    public void eliminar(Long id) {
        matriculaRepository.deleteById(id);
    }
}
