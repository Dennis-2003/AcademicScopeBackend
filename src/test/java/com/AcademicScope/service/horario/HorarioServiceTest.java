package com.AcademicScope.service.horario;

import com.AcademicScope.model.Horario;
import com.AcademicScope.repository.horario.HorarioRepository;
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
class HorarioServiceTest {

    @Mock
    private HorarioRepository horarioRepository;
    @InjectMocks
    private HorarioService horarioService;

    private Horario horario;

    @BeforeEach
    void setUp() {
        horario = Horario.builder()
                .id(1L)
                .diaSemana("LUNES")
                .horaInicio("08:00")
                .horaFin("09:30")
                .aula("A-101")
                .build();
    }

    @Test
    void deberia_crear_horario() {
        when(horarioRepository.save(any())).thenReturn(horario);

        Horario resultado = horarioService.crear(horario);

        assertEquals("LUNES", resultado.getDiaSemana());
        verify(horarioRepository).save(horario);
    }

    @Test
    void deberia_listar_por_curso() {
        when(horarioRepository.findByCursoId(1L)).thenReturn(List.of(horario));

        List<Horario> resultado = horarioService.listarPorCurso(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_por_docente() {
        when(horarioRepository.findByCursoDocenteId(1L)).thenReturn(List.of(horario));

        List<Horario> resultado = horarioService.listarPorDocente(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_todos() {
        when(horarioRepository.findAll()).thenReturn(List.of(horario));

        List<Horario> resultado = horarioService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_obtener_por_id() {
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(horario));

        Horario resultado = horarioService.obtenerPorId(1L);

        assertNotNull(resultado);
    }

    @Test
    void deberia_lanzar_excepcion_si_horario_no_existe() {
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> horarioService.obtenerPorId(99L));
    }

    @Test
    void deberia_actualizar_campos_no_nulos() {
        Horario actualizado = Horario.builder()
                .diaSemana("MARTES")
                .horaInicio("10:00")
                .aula("B-202")
                .build();

        when(horarioRepository.findById(1L)).thenReturn(Optional.of(horario));
        when(horarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Horario resultado = horarioService.actualizar(1L, actualizado);

        assertEquals("MARTES", resultado.getDiaSemana());
        assertEquals("10:00", resultado.getHoraInicio());
        assertEquals("09:30", resultado.getHoraFin()); // no se actualiza
        assertEquals("B-202", resultado.getAula());
    }

    @Test
    void deberia_eliminar_horario() {
        horarioService.eliminar(1L);
        verify(horarioRepository).deleteById(1L);
    }
}
