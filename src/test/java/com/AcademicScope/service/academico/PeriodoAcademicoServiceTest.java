package com.AcademicScope.service.academico;

import com.AcademicScope.model.PeriodoAcademico;
import com.AcademicScope.repository.academico.PeriodoAcademicoRepository;
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
class PeriodoAcademicoServiceTest {

    @Mock
    private PeriodoAcademicoRepository periodoRepository;
    @InjectMocks
    private PeriodoAcademicoService periodoService;

    private PeriodoAcademico periodo;

    @BeforeEach
    void setUp() {
        periodo = PeriodoAcademico.builder()
                .id(1L)
                .nombre("2025-I")
                .fechaInicio(LocalDate.of(2025, 3, 1))
                .fechaFin(LocalDate.of(2025, 7, 30))
                .activo(true)
                .build();
    }

    @Test
    void deberia_crear_periodo() {
        when(periodoRepository.save(any())).thenReturn(periodo);

        PeriodoAcademico resultado = periodoService.crear(periodo);

        assertEquals("2025-I", resultado.getNombre());
        assertTrue(resultado.getActivo());
    }

    @Test
    void deberia_actualizar_periodo_existente() {
        PeriodoAcademico actualizado = PeriodoAcademico.builder()
                .nombre("2025-II")
                .fechaInicio(LocalDate.of(2025, 8, 1))
                .fechaFin(LocalDate.of(2025, 12, 20))
                .activo(false)
                .build();

        when(periodoRepository.findById(1L)).thenReturn(Optional.of(periodo));
        when(periodoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PeriodoAcademico resultado = periodoService.actualizar(1L, actualizado);

        assertEquals("2025-II", resultado.getNombre());
        assertFalse(resultado.getActivo());
    }

    @Test
    void deberia_lanzar_excepcion_al_actualizar_periodo_inexistente() {
        when(periodoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> periodoService.actualizar(99L, new PeriodoAcademico()));
    }

    @Test
    void deberia_obtener_periodo_por_id() {
        when(periodoRepository.findById(1L)).thenReturn(Optional.of(periodo));

        PeriodoAcademico resultado = periodoService.obtenerPorId(1L);

        assertEquals("2025-I", resultado.getNombre());
    }

    @Test
    void deberia_obtener_periodo_activo() {
        when(periodoRepository.findByActivoTrue()).thenReturn(Optional.of(periodo));

        PeriodoAcademico resultado = periodoService.obtenerActivo();

        assertTrue(resultado.getActivo());
    }

    @Test
    void deberia_lanzar_excepcion_si_no_hay_periodo_activo() {
        when(periodoRepository.findByActivoTrue()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> periodoService.obtenerActivo());
    }

    @Test
    void deberia_listar_todos_los_periodos() {
        when(periodoRepository.findAll()).thenReturn(List.of(periodo));

        List<PeriodoAcademico> resultado = periodoService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_eliminar_periodo() {
        periodoService.eliminar(1L);
        verify(periodoRepository).deleteById(1L);
    }
}
