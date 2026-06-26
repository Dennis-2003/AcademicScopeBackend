package com.AcademicScope.service.dashboard;

import com.AcademicScope.dto.DashboardRendimientoDTO;
import com.AcademicScope.dto.TutorDashboardDTO;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.model.*;
import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import com.AcademicScope.repository.matricula.MatriculaRepository;
import com.AcademicScope.repository.evaluacion.CalificacionRepository;
import com.AcademicScope.repository.notificacion.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private CursoRepository cursoRepository;
    @Mock
    private AsistenciaRepository asistenciaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private CalificacionRepository calificacionRepository;
    @Mock
    private NotificacionRepository notificacionRepository;
    @InjectMocks
    private DashboardService dashboardService;

    private Usuario tutor;
    private Usuario hijo;
    private Curso curso;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        tutor = Usuario.builder().id(1L).nombre("Ana").apellido("López").build();
        hijo = Usuario.builder().id(2L).nombre("Pedro").apellido("García").tutor(tutor).build();
        Grado grado = Grado.builder().id(1L).nombre("1° Primaria").build();
        curso = Curso.builder().id(1L).nombre("Matemáticas").grado(grado).build();
        matricula = Matricula.builder()
                .grado(grado)
                .estado(com.AcademicScope.enums.EstadoMatricula.ACTIVA)
                .build();
    }

    @Test
    void deberia_obtener_rendimiento_con_7_meses() {
        when(cursoRepository.findAll()).thenReturn(List.of(curso));
        when(asistenciaRepository.findAll()).thenReturn(List.of());

        DashboardRendimientoDTO resultado = dashboardService.obtenerRendimiento();

        assertEquals(7, resultado.getMeses().size());
        assertEquals(7, resultado.getCursosActivos().size());
        assertEquals(7, resultado.getAsistenciaMedia().size());
    }

    @Test
    void deberia_obtener_dashboard_tutor_sin_hijos() {
        when(usuarioRepository.findByTutorId(1L)).thenReturn(List.of());
        when(notificacionRepository.findByUsuarioIdAndLeidoFalse(1L)).thenReturn(List.of());

        TutorDashboardDTO resultado = dashboardService.obtenerDashboardTutor(1L);

        assertEquals(0, resultado.getHijos());
        assertEquals(0, resultado.getNotificaciones());
        assertTrue(resultado.getResumenHijos().isEmpty());
        assertTrue(resultado.getAlertas().isEmpty());
    }

    @Test
    void deberia_obtener_dashboard_tutor_con_hijo_sin_notas() {
        when(usuarioRepository.findByTutorId(1L)).thenReturn(List.of(hijo));
        when(notificacionRepository.findByUsuarioIdAndLeidoFalse(1L)).thenReturn(List.of());
        when(calificacionRepository.findByEstudianteId(2L)).thenReturn(List.of());
        when(matriculaRepository.findByEstudianteId(2L)).thenReturn(List.of(matricula));

        TutorDashboardDTO resultado = dashboardService.obtenerDashboardTutor(1L);

        assertEquals(1, resultado.getHijos());
        assertEquals("GRIS", resultado.getResumenHijos().get(0).getSemaforo());
        assertEquals(100, resultado.getResumenHijos().get(0).getAsistencia());
    }

    @Test
    void deberia_calcular_semaforo_verde_con_nota_alta() {
        Evaluacion evaluacion = Evaluacion.builder().curso(curso).nombre("Examen").build();
        Calificacion cal = Calificacion.builder().nota(16.0).evaluacion(evaluacion).build();

        when(usuarioRepository.findByTutorId(1L)).thenReturn(List.of(hijo));
        when(notificacionRepository.findByUsuarioIdAndLeidoFalse(1L)).thenReturn(List.of());
        when(calificacionRepository.findByEstudianteId(2L)).thenReturn(List.of(cal));
        when(matriculaRepository.findByEstudianteId(2L)).thenReturn(List.of(matricula));

        TutorDashboardDTO resultado = dashboardService.obtenerDashboardTutor(1L);

        assertEquals("VERDE", resultado.getResumenHijos().get(0).getSemaforo());
        assertTrue(resultado.getAlertas().isEmpty());
    }

    @Test
    void deberia_generar_alerta_por_bajo_rendimiento() {
        Evaluacion evaluacion = Evaluacion.builder().curso(curso).nombre("Examen").build();
        Calificacion cal = Calificacion.builder().nota(10.0).evaluacion(evaluacion).build();

        when(usuarioRepository.findByTutorId(1L)).thenReturn(List.of(hijo));
        when(notificacionRepository.findByUsuarioIdAndLeidoFalse(1L)).thenReturn(List.of());
        when(calificacionRepository.findByEstudianteId(2L)).thenReturn(List.of(cal));
        when(matriculaRepository.findByEstudianteId(2L)).thenReturn(List.of(matricula));

        TutorDashboardDTO resultado = dashboardService.obtenerDashboardTutor(1L);

        assertFalse(resultado.getAlertas().isEmpty());
        assertTrue(resultado.getAlertas().get(0).getTitulo().contains("Bajo rendimiento"));
    }

    @Test
    void deberia_limitar_alertas_a_5() {
        when(usuarioRepository.findByTutorId(1L)).thenReturn(List.of(hijo));
        when(notificacionRepository.findByUsuarioIdAndLeidoFalse(1L)).thenReturn(List.of());
        when(matriculaRepository.findByEstudianteId(2L)).thenReturn(List.of(matricula));

        Evaluacion eval = Evaluacion.builder().curso(curso).nombre("Eval").build();
        List<Calificacion> muchasNotas = java.util.stream.IntStream.range(0, 10)
                .mapToObj(i -> Calificacion.builder().nota(8.0).evaluacion(eval).build())
                .toList();
        when(calificacionRepository.findByEstudianteId(2L)).thenReturn(muchasNotas);

        TutorDashboardDTO resultado = dashboardService.obtenerDashboardTutor(1L);

        assertTrue(resultado.getAlertas().size() <= 5);
    }
}
