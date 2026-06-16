package com.AcademicScope.service.asignacion;

import com.AcademicScope.model.Asignacion;
import com.AcademicScope.model.EntregaAsignacion;
import com.AcademicScope.repository.asignacion.AsignacionRepository;
import com.AcademicScope.repository.asignacion.EntregaAsignacionRepository;
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
class AsignacionServiceTest {

    @Mock
    private AsignacionRepository asignacionRepository;
    @Mock
    private EntregaAsignacionRepository entregaRepository;
    @InjectMocks
    private AsignacionService asignacionService;

    private Asignacion asignacion;

    @BeforeEach
    void setUp() {
        asignacion = Asignacion.builder()
                .id(1L)
                .titulo("Tarea 1")
                .estado("ACTIVA")
                .build();
    }

    @Test
    void deberia_crear_asignacion_con_fecha_registro_y_estado_activo() {
        when(asignacionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Asignacion resultado = asignacionService.crear(asignacion);

        assertNotNull(resultado.getFechaRegistro());
        assertEquals("ACTIVA", resultado.getEstado());
        verify(asignacionRepository).save(asignacion);
    }

    @Test
    void deberia_crear_asignacion_respetando_estado_existente() {
        asignacion.setEstado("COMPLETADA");
        when(asignacionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Asignacion resultado = asignacionService.crear(asignacion);

        assertEquals("COMPLETADA", resultado.getEstado());
    }

    @Test
    void deberia_listar_por_curso() {
        when(asignacionRepository.findByCursoId(1L)).thenReturn(List.of(asignacion));

        List<Asignacion> resultado = asignacionService.listarPorCurso(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_obtener_por_id() {
        when(asignacionRepository.findById(1L)).thenReturn(Optional.of(asignacion));

        Asignacion resultado = asignacionService.obtenerPorId(1L);

        assertEquals("Tarea 1", resultado.getTitulo());
    }

    @Test
    void deberia_lanzar_excepcion_si_asignacion_no_existe() {
        when(asignacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> asignacionService.obtenerPorId(99L));
    }

    @Test
    void deberia_obtener_entregas_por_asignacion() {
        when(entregaRepository.findByAsignacionId(1L)).thenReturn(List.of(new EntregaAsignacion()));

        List<EntregaAsignacion> resultado = asignacionService.obtenerEntregas(1L);

        assertEquals(1, resultado.size());
        verify(entregaRepository).findByAsignacionId(1L);
    }

    @Test
    void deberia_listar_por_estudiante() {
        when(asignacionRepository.findByEstudianteId(1L)).thenReturn(List.of(asignacion));

        List<Asignacion> resultado = asignacionService.listarPorEstudiante(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_eliminar_asignacion() {
        asignacionService.eliminar(1L);
        verify(asignacionRepository).deleteById(1L);
    }
}
