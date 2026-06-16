package com.AcademicScope.service.academico;

import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Grado;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.repository.academico.CursoRepository;
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
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;
    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private Grado grado;

    @BeforeEach
    void setUp() {
        grado = Grado.builder().id(1L).nombre("1° Primaria").build();
        curso = Curso.builder()
                .id(1L)
                .nombre("Matemáticas")
                .codigo("MAT-101")
                .tipo(TipoCurso.REGULAR)
                .grado(grado)
                .build();
    }

    @Test
    void deberia_crear_curso() {
        when(cursoRepository.save(any())).thenReturn(curso);

        Curso resultado = cursoService.crear(curso);

        assertEquals("Matemáticas", resultado.getNombre());
        verify(cursoRepository).save(curso);
    }

    @Test
    void deberia_actualizar_curso_existente() {
        Curso actualizado = Curso.builder()
                .nombre("Matemáticas Avanzadas")
                .descripcion("Curso avanzado")
                .codigo("MAT-201")
                .tipo(TipoCurso.TALLER)
                .build();

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Curso resultado = cursoService.actualizar(1L, actualizado);

        assertEquals("Matemáticas Avanzadas", resultado.getNombre());
        assertEquals(TipoCurso.TALLER, resultado.getTipo());
    }

    @Test
    void deberia_lanzar_excepcion_al_actualizar_curso_inexistente() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> cursoService.actualizar(99L, new Curso()));
    }

    @Test
    void deberia_obtener_curso_por_id() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        Curso resultado = cursoService.obtenerPorId(1L);

        assertEquals("Matemáticas", resultado.getNombre());
    }

    @Test
    void deberia_lanzar_excepcion_al_obtener_curso_inexistente() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cursoService.obtenerPorId(99L));
    }

    @Test
    void deberia_listar_todos_los_cursos() {
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_cursos_por_grado() {
        when(cursoRepository.findByGradoId(1L)).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listarPorGrado(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_cursos_por_docente() {
        when(cursoRepository.findByDocenteId(2L)).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listarPorDocente(2L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_cursos_por_tipo() {
        when(cursoRepository.findByTipo(TipoCurso.REGULAR)).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listarPorTipo(TipoCurso.REGULAR);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_eliminar_curso() {
        cursoService.eliminar(1L);
        verify(cursoRepository).deleteById(1L);
    }
}
