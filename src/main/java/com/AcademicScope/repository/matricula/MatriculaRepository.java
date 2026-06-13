package com.AcademicScope.repository.matricula;

import com.AcademicScope.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByEstudianteId(Long estudianteId);
    List<Matricula> findByCursoGradoId(Long gradoId);
    List<Matricula> findByPeriodoId(Long periodoId);
}
