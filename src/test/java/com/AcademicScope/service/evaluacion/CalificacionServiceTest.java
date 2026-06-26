package com.AcademicScope.service.evaluacion;

import com.AcademicScope.model.Calificacion;
import com.AcademicScope.enums.ColorPerformance;
import com.AcademicScope.repository.evaluacion.CalificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;
    @InjectMocks
    private CalificacionService calificacionService;

    private Calificacion calificacion;

    @BeforeEach
    void setUp() {
        calificacion = Calificacion.builder()
                .id(1L)
                .nota(15.0)
                .comentarioDocente("Buen trabajo")
                .build();
    }

    @Test
    void deberia_calificar_y_guardar() {
        when(calificacionRepository.save(any())).thenReturn(calificacion);

        Calificacion resultado = calificacionService.calificar(calificacion);

        assertEquals(15.0, resultado.getNota());
        verify(calificacionRepository).save(calificacion);
    }

    @Test
    void deberia_actualizar_calificacion_existente() {
        Calificacion actualizado = Calificacion.builder()
                .nota(18.0)
                .comentarioDocente("Excelente")
                .recomendacion("Seguir así")
                .aspectoConducta("Bueno")
                .build();

        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));
        when(calificacionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Calificacion resultado = calificacionService.actualizar(1L, actualizado);

        assertEquals(18.0, resultado.getNota());
        assertEquals("Excelente", resultado.getComentarioDocente());
    }

    @Test
    void deberia_lanzar_excepcion_al_actualizar_calificacion_inexistente() {
        when(calificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> calificacionService.actualizar(99L, new Calificacion()));
    }

    @Test
    void deberia_obtener_calificacion_por_id() {
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        Calificacion resultado = calificacionService.obtenerPorId(1L);

        assertEquals(15.0, resultado.getNota());
    }

    @Test
    void deberia_listar_calificaciones_por_evaluacion() {
        when(calificacionRepository.findByEvaluacionId(1L)).thenReturn(List.of(calificacion));

        List<Calificacion> resultado = calificacionService.listarPorEvaluacion(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_calificaciones_por_estudiante() {
        when(calificacionRepository.findByEstudianteId(1L)).thenReturn(List.of(calificacion));

        List<Calificacion> resultado = calificacionService.listarPorEstudiante(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_eliminar_calificacion() {
        calificacionService.eliminar(1L);
        verify(calificacionRepository).deleteById(1L);
    }

    @ParameterizedTest
    @CsvSource({
        "20, AZUL",
        "19, AZUL",
        "18, AZUL",
        "17, VERDE",
        "15, VERDE",
        "14, VERDE",
        "13, AMARILLO",
        "12, AMARILLO",
        "11, AMARILLO",
        "10, ROJO",
        "5, ROJO",
        "0, ROJO",
        ", GRIS"
    })
    void deberia_calcular_color_correctamente(Double nota, ColorPerformance esperado) {
        assertEquals(esperado, calificacionService.calcularColor(nota));
    }
}
