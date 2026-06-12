package com.AcademicScope.repository;

import com.AcademicScope.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByEvaluacionId(Long evaluacionId);
    List<Calificacion> findByEstudianteId(Long estudianteId);
}
