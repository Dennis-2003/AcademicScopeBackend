package com.AcademicScope.repository.academico;

import com.AcademicScope.model.Curso;
import com.AcademicScope.enums.TipoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByGradoId(Long gradoId);
    List<Curso> findByDocenteId(Long docenteId);
    List<Curso> findByTipo(TipoCurso tipo);
}
