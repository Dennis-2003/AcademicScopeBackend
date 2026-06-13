package com.AcademicScope.repository.finanzas;

import com.AcademicScope.model.PagoEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoEstudianteRepository extends JpaRepository<PagoEstudiante, Long> {
    List<PagoEstudiante> findByEstudianteId(Long estudianteId);
    Optional<PagoEstudiante> findByEstudianteIdAndConceptoId(Long estudianteId, Long conceptoId);
}
