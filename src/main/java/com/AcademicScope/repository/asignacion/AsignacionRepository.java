package com.AcademicScope.repository.asignacion;

import com.AcademicScope.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByCursoId(Long cursoId);

    @Query("SELECT a FROM Asignacion a WHERE a.curso.grado IN (SELECT m.grado FROM Matricula m WHERE m.estudiante.id = :estudianteId) ORDER BY a.fechaVencimiento DESC")
    List<Asignacion> findByEstudianteId(@Param("estudianteId") Long estudianteId);
}
