package com.AcademicScope.repository.institucion;

import com.AcademicScope.model.Institucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Long> {
    Optional<Institucion> findByRuc(String ruc);
}
