package com.AcademicScope.repository.asistencia;

import com.AcademicScope.model.Asistencia;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.enums.TipoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByEstudianteIdAndCursoId(Long estudianteId, Long cursoId);
    List<Asistencia> findByCursoIdAndFecha(Long cursoId, LocalDate fecha);
    long countByEstudianteIdAndCursoIdAndTipo(Long estudianteId, Long cursoId, TipoAsistencia tipo);

    @Query("SELECT COUNT(a) FROM Asistencia a WHERE " +
           "(:gradoId IS NULL OR a.curso.grado.id = :gradoId) AND " +
           "(:tipoCurso IS NULL OR a.curso.tipo = :tipoCurso) AND " +
           "a.tipo = :tipoAsistencia")
    long countByFiltros(@Param("gradoId") Long gradoId, 
                        @Param("tipoCurso") TipoCurso tipoCurso, 
                        @Param("tipoAsistencia") TipoAsistencia tipoAsistencia);

    @Query("SELECT a.curso, COUNT(a) as faltas FROM Asistencia a WHERE " +
           "(:gradoId IS NULL OR a.curso.grado.id = :gradoId) AND " +
           "(:tipoCurso IS NULL OR a.curso.tipo = :tipoCurso) AND " +
           "a.tipo = 'FALTA' " +
           "GROUP BY a.curso " +
           "ORDER BY faltas DESC")
    List<Object[]> findTopCursosConFaltas(@Param("gradoId") Long gradoId, 
                                          @Param("tipoCurso") TipoCurso tipoCurso);

    @Query("SELECT a.estudiante, a.curso.grado.nombre, COUNT(a) as faltas FROM Asistencia a WHERE " +
           "(:gradoId IS NULL OR a.curso.grado.id = :gradoId) AND " +
           "(:tipoCurso IS NULL OR a.curso.tipo = :tipoCurso) AND " +
           "a.tipo = 'FALTA' " +
           "GROUP BY a.estudiante, a.curso.grado.nombre " +
           "ORDER BY faltas DESC")
    List<Object[]> findTopEstudiantesConFaltas(@Param("gradoId") Long gradoId, 
                                               @Param("tipoCurso") TipoCurso tipoCurso);
}
