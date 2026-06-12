package com.AcademicScope.repository;

import com.AcademicScope.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {
    Optional<PeriodoAcademico> findByActivoTrue();
}
