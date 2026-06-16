package com.AcademicScope.service.recurso;

import com.AcademicScope.model.Recurso;
import com.AcademicScope.repository.recurso.RecursoRepository;
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
class RecursoServiceTest {

    @Mock
    private RecursoRepository recursoRepository;
    @InjectMocks
    private RecursoService recursoService;

    private Recurso recurso;

    @BeforeEach
    void setUp() {
        recurso = Recurso.builder()
                .id(1L)
                .titulo("Guía de estudio")
                .tipo("PDF")
                .url("https://recursos.com/guia.pdf")
                .build();
    }

    @Test
    void deberia_crear_recurso_con_fecha_de_subida() {
        when(recursoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Recurso resultado = recursoService.crear(recurso);

        assertNotNull(resultado.getFechaSubida());
        verify(recursoRepository).save(recurso);
    }

    @Test
    void deberia_listar_recursos_por_curso() {
        when(recursoRepository.findByCursoId(1L)).thenReturn(List.of(recurso));

        List<Recurso> resultado = recursoService.listarPorCurso(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_obtener_recurso_por_id() {
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        Recurso resultado = recursoService.obtenerPorId(1L);

        assertEquals("Guía de estudio", resultado.getTitulo());
    }

    @Test
    void deberia_lanzar_excepcion_si_no_existe() {
        when(recursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> recursoService.obtenerPorId(99L));
    }

    @Test
    void deberia_eliminar_recurso() {
        recursoService.eliminar(1L);
        verify(recursoRepository).deleteById(1L);
    }
}
