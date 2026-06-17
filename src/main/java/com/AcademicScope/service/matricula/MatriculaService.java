package com.AcademicScope.service.matricula;

import com.AcademicScope.repository.matricula.MatriculaRepository;
import com.AcademicScope.model.Matricula;
import com.AcademicScope.enums.EstadoMatricula;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final com.AcademicScope.repository.academico.CursoRepository cursoRepository;

    public Matricula matricular(Matricula matricula) {
        matricula.setEstado(EstadoMatricula.ACTIVA);
        matricula.setFechaMatricula(LocalDate.now());
        matricula = matriculaRepository.save(matricula);
        return matriculaRepository.findById(matricula.getId())
                .orElseThrow(() -> new RuntimeException("Error al recuperar matrícula"));
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

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public List<Matricula> listarPorGrado(Long gradoId) {
        return matriculaRepository.findByGradoId(gradoId);
    }

    public List<Matricula> listarPorCurso(Long cursoId) {
        com.AcademicScope.model.Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso != null && curso.getGrado() != null) {
            return matriculaRepository.findByGradoId(curso.getGrado().getId());
        }
        return List.of();
    }

    public List<Matricula> listarPorPeriodo(Long periodoId) {
        return matriculaRepository.findByPeriodoId(periodoId);
    }

    public void eliminar(Long id) {
        matriculaRepository.deleteById(id);
    }
}
