package com.AcademicScope.service.comportamiento;

import com.AcademicScope.model.Comportamiento;
import com.AcademicScope.repository.comportamiento.ComportamientoRepository;
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
class ComportamientoServiceTest {

    @Mock
    private ComportamientoRepository comportamientoRepository;
    @InjectMocks
    private ComportamientoService comportamientoService;

    private Comportamiento comportamiento;

    @BeforeEach
    void setUp() {
        comportamiento = Comportamiento.builder()
                .id(1L)
                .descripcion("Buen comportamiento")
                .puntaje(5)
                .build();
    }

    @Test
    void deberia_registrar_comportamiento_con_fecha_actual() {
        when(comportamientoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Comportamiento resultado = comportamientoService.registrar(comportamiento);

        assertNotNull(resultado.getFecha());
        assertEquals(LocalDate.now(), resultado.getFecha());
        verify(comportamientoRepository).save(comportamiento);
    }

    @Test
    void deberia_listar_todos_los_comportamientos() {
        when(comportamientoRepository.findAllByOrderByFechaDescIdDesc()).thenReturn(List.of(comportamiento));

        List<Comportamiento> resultado = comportamientoService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_obtener_comportamiento_por_id() {
        when(comportamientoRepository.findById(1L)).thenReturn(Optional.of(comportamiento));

        Comportamiento resultado = comportamientoService.obtenerPorId(1L);

        assertNotNull(resultado);
    }

    @Test
    void deberia_lanzar_excepcion_si_no_existe() {
        when(comportamientoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> comportamientoService.obtenerPorId(99L));
    }

    @Test
    void deberia_listar_por_estudiante_y_periodo() {
        when(comportamientoRepository.findByEstudianteIdAndPeriodoId(1L, 1L))
                .thenReturn(List.of(comportamiento));

        List<Comportamiento> resultado = comportamientoService.listarPorEstudianteYPeriodo(1L, 1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_por_estudiante() {
        when(comportamientoRepository.findByEstudianteId(1L)).thenReturn(List.of(comportamiento));

        List<Comportamiento> resultado = comportamientoService.listarPorEstudiante(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_eliminar_comportamiento() {
        comportamientoService.eliminar(1L);
        verify(comportamientoRepository).deleteById(1L);
    }
}
