package com.AcademicScope.repository.academico;

import com.AcademicScope.model.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Long> {
    List<Grado> findByNivel(String nivel);
    boolean existsByNombre(String nombre);
}
