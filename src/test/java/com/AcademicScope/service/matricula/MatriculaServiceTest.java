package com.AcademicScope.service.matricula;

import com.AcademicScope.enums.EstadoMatricula;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Grado;
import com.AcademicScope.model.Matricula;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.repository.matricula.MatriculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private CursoRepository cursoRepository;
    @InjectMocks
    private MatriculaService matriculaService;

    private Matricula matricula;
    private Grado grado;

    @BeforeEach
    void setUp() {
        grado = Grado.builder().id(1L).nombre("1° Primaria").build();
        matricula = Matricula.builder()
                .id(1L)
                .seccion("A")
                .estado(EstadoMatricula.ACTIVA)
                .fechaMatricula(LocalDate.now())
                .grado(grado)
                .build();
    }

    @Test
    void deberia_matricular_con_estado_activo_y_fecha_actual() {
        when(matriculaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Matricula resultado = matriculaService.matricular(matricula);

        assertEquals(EstadoMatricula.ACTIVA, resultado.getEstado());
        assertNotNull(resultado.getFechaMatricula());
        assertEquals(LocalDate.now(), resultado.getFechaMatricula());
    }

    @Test
    void deberia_retirar_matricula_existente() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Matricula resultado = matriculaService.retirar(1L);

        assertEquals(EstadoMatricula.RETIRADA, resultado.getEstado());
    }

    @Test
    void deberia_lanzar_excepcion_al_retirar_matricula_inexistente() {
        when(matriculaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matriculaService.retirar(99L));
    }

    @Test
    void deberia_obtener_matricula_por_id() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));

        Matricula resultado = matriculaService.obtenerPorId(1L);

        assertNotNull(resultado);
    }

    @Test
    void deberia_listar_por_estudiante() {
        when(matriculaRepository.findByEstudianteId(1L)).thenReturn(List.of(matricula));

        List<Matricula> resultado = matriculaService.listarPorEstudiante(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_todas() {
        when(matriculaRepository.findAll()).thenReturn(List.of(matricula));

        List<Matricula> resultado = matriculaService.listarTodas();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_por_grado() {
        when(matriculaRepository.findByGradoId(1L)).thenReturn(List.of(matricula));

        List<Matricula> resultado = matriculaService.listarPorGrado(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_por_periodo() {
        when(matriculaRepository.findByPeriodoId(1L)).thenReturn(List.of(matricula));

        List<Matricula> resultado = matriculaService.listarPorPeriodo(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_por_curso_usando_grado() {
        Curso curso = Curso.builder().id(1L).grado(grado).build();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(matriculaRepository.findByGradoId(1L)).thenReturn(List.of(matricula));

        List<Matricula> resultado = matriculaService.listarPorCurso(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_retornar_lista_vacia_si_curso_sin_grado() {
        Curso curso = Curso.builder().id(1L).build();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        List<Matricula> resultado = matriculaService.listarPorCurso(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deberia_eliminar_matricula() {
        matriculaService.eliminar(1L);
        verify(matriculaRepository).deleteById(1L);
    }
}
