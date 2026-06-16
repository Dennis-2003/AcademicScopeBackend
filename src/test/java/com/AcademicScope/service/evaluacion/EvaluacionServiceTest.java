package com.AcademicScope.service.evaluacion;

import com.AcademicScope.model.Evaluacion;
import com.AcademicScope.repository.evaluacion.EvaluacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;
    @InjectMocks
    private EvaluacionService evaluacionService;

    private Evaluacion evaluacion;

    @BeforeEach
    void setUp() {
        evaluacion = Evaluacion.builder()
                .id(1L)
                .nombre("Examen Parcial")
                .ponderacion(0.3)
                .orden(1)
                .build();
    }

    @Test
    void deberia_crear_evaluacion() {
        when(evaluacionRepository.save(any())).thenReturn(evaluacion);

        Evaluacion resultado = evaluacionService.crear(evaluacion);

        assertEquals("Examen Parcial", resultado.getNombre());
    }

    @Test
    void deberia_actualizar_evaluacion_existente() {
        Evaluacion actualizado = Evaluacion.builder()
                .nombre("Examen Final")
                .descripcion("Evaluación final")
                .ponderacion(0.5)
                .orden(2)
                .build();

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(evaluacionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Evaluacion resultado = evaluacionService.actualizar(1L, actualizado);

        assertEquals("Examen Final", resultado.getNombre());
        assertEquals(0.5, resultado.getPonderacion());
    }

    @Test
    void deberia_lanzar_excepcion_al_actualizar_evaluacion_inexistente() {
        when(evaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> evaluacionService.actualizar(99L, new Evaluacion()));
    }

    @Test
    void deberia_obtener_evaluacion_por_id() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        Evaluacion resultado = evaluacionService.obtenerPorId(1L);

        assertNotNull(resultado);
    }

    @Test
    void deberia_listar_evaluaciones_por_curso_y_periodo() {
        when(evaluacionRepository.findByCursoIdAndPeriodoId(1L, 1L)).thenReturn(List.of(evaluacion));

        List<Evaluacion> resultado = evaluacionService.listarPorCursoYPeriodo(1L, 1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_evaluaciones_por_curso() {
        when(evaluacionRepository.findByCursoId(1L)).thenReturn(List.of(evaluacion));

        List<Evaluacion> resultado = evaluacionService.listarPorCurso(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_proximas_evaluaciones_por_docente() {
        when(evaluacionRepository.findByCursoDocenteIdOrderByFechaAsc(1L)).thenReturn(List.of(evaluacion));

        List<Evaluacion> resultado = evaluacionService.listarProximasPorDocente(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_eliminar_evaluacion() {
        evaluacionService.eliminar(1L);
        verify(evaluacionRepository).deleteById(1L);
    }
}
