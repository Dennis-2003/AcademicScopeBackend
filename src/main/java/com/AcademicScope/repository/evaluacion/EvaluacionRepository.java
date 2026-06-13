package com.AcademicScope.repository.evaluacion;

import com.AcademicScope.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByCursoIdAndPeriodoId(Long cursoId, Long periodoId);
    List<Evaluacion> findByCursoId(Long cursoId);
}
