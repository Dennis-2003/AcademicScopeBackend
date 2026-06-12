package com.AcademicScope.repository;

import com.AcademicScope.model.Asistencia;
import com.AcademicScope.enums.TipoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByEstudianteIdAndCursoId(Long estudianteId, Long cursoId);
    List<Asistencia> findByCursoIdAndFecha(Long cursoId, LocalDate fecha);
    long countByEstudianteIdAndCursoIdAndTipo(Long estudianteId, Long cursoId, TipoAsistencia tipo);
}
