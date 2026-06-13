package com.AcademicScope.service.academico;

import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.model.Curso;
import com.AcademicScope.enums.TipoCurso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public Curso crear(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso actualizar(Long id, Curso curso) {
        return cursoRepository.findById(id).map(c -> {
            c.setNombre(curso.getNombre());
            c.setDescripcion(curso.getDescripcion());
            c.setCodigo(curso.getCodigo());
            c.setTipo(curso.getTipo());
            c.setGrado(curso.getGrado());
            c.setDocente(curso.getDocente());
            return cursoRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public List<Curso> listarPorGrado(Long gradoId) {
        return cursoRepository.findByGradoId(gradoId);
    }

    public List<Curso> listarPorDocente(Long docenteId) {
        return cursoRepository.findByDocenteId(docenteId);
    }

    public List<Curso> listarPorTipo(TipoCurso tipo) {
        return cursoRepository.findByTipo(tipo);
    }

    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }
}
