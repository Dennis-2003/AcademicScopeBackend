package com.AcademicScope.repository;

import com.AcademicScope.model.EntregaAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaAsignacionRepository extends JpaRepository<EntregaAsignacion, Long> {
    List<EntregaAsignacion> findByAsignacionId(Long asignacionId);
    List<EntregaAsignacion> findByEstudianteId(Long estudianteId);
}
