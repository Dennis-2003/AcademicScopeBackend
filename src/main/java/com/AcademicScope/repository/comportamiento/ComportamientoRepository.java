package com.AcademicScope.repository.comportamiento;

import com.AcademicScope.model.Comportamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComportamientoRepository extends JpaRepository<Comportamiento, Long> {
    List<Comportamiento> findByEstudianteIdAndPeriodoId(Long estudianteId, Long periodoId);
    List<Comportamiento> findByEstudianteId(Long estudianteId);
}
