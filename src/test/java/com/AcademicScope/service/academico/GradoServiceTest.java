package com.AcademicScope.service.academico;

import com.AcademicScope.model.Grado;
import com.AcademicScope.repository.academico.GradoRepository;
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
class GradoServiceTest {

    @Mock
    private GradoRepository gradoRepository;
    @InjectMocks
    private GradoService gradoService;

    private Grado grado;

    @BeforeEach
    void setUp() {
        grado = Grado.builder()
                .id(1L)
                .nombre("1° Primaria")
                .nivel("PRIMARIA")
                .build();
    }

    @Test
    void deberia_crear_grado() {
        when(gradoRepository.save(any())).thenReturn(grado);

        Grado resultado = gradoService.crear(grado);

        assertEquals("1° Primaria", resultado.getNombre());
        verify(gradoRepository).save(grado);
    }

    @Test
    void deberia_actualizar_grado_existente() {
        Grado actualizado = Grado.builder()
                .nombre("2° Primaria")
                .nivel("PRIMARIA")
                .build();

        when(gradoRepository.findById(1L)).thenReturn(Optional.of(grado));
        when(gradoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Grado resultado = gradoService.actualizar(1L, actualizado);

        assertEquals("2° Primaria", resultado.getNombre());
        assertEquals("PRIMARIA", resultado.getNivel());
    }

    @Test
    void deberia_lanzar_excepcion_al_actualizar_grado_inexistente() {
        when(gradoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> gradoService.actualizar(99L, new Grado()));
    }

    @Test
    void deberia_obtener_grado_por_id() {
        when(gradoRepository.findById(1L)).thenReturn(Optional.of(grado));

        Grado resultado = gradoService.obtenerPorId(1L);

        assertEquals("1° Primaria", resultado.getNombre());
    }

    @Test
    void deberia_lanzar_excepcion_al_obtener_grado_inexistente() {
        when(gradoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gradoService.obtenerPorId(99L));
    }

    @Test
    void deberia_listar_todos_los_grados() {
        when(gradoRepository.findAll()).thenReturn(List.of(grado));

        List<Grado> resultado = gradoService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_grados_por_nivel() {
        when(gradoRepository.findByNivel("PRIMARIA")).thenReturn(List.of(grado));

        List<Grado> resultado = gradoService.listarPorNivel("PRIMARIA");

        assertEquals(1, resultado.size());
        verify(gradoRepository).findByNivel("PRIMARIA");
    }

    @Test
    void deberia_eliminar_grado() {
        gradoService.eliminar(1L);
        verify(gradoRepository).deleteById(1L);
    }
}
