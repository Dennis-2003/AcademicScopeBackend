package com.AcademicScope.service.asistencia;

import com.AcademicScope.dto.ReporteAsistenciaDTO;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.model.*;
import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.repository.notificacion.NotificacionRepository;
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
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;
    @Mock
    private NotificacionRepository notificacionRepository;
    @InjectMocks
    private AsistenciaService asistenciaService;

    private Asistencia asistencia;
    private Usuario estudiante;
    private Usuario tutor;
    private Curso curso;

    @BeforeEach
    void setUp() {
        tutor = Usuario.builder().id(2L).nombre("Ana").apellido("López").build();
        estudiante = Usuario.builder().id(1L).nombre("Pedro").apellido("García").tutor(tutor).build();
        curso = Curso.builder().id(1L).nombre("Matemáticas").build();

        asistencia = Asistencia.builder()
                .id(1L)
                .estudiante(estudiante)
                .curso(curso)
                .fecha(LocalDate.now())
                .tipo(TipoAsistencia.PRESENTE)
                .build();
    }

    @Test
    void deberia_registrar_asistencia_presente_sin_notificar() {
        when(asistenciaRepository.save(any())).thenReturn(asistencia);

        Asistencia resultado = asistenciaService.registrar(asistencia);

        assertEquals(TipoAsistencia.PRESENTE, resultado.getTipo());
        verify(asistenciaRepository).save(asistencia);
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void deberia_notificar_al_tutor_cuando_faltas_alcanzan_tres() {
        Asistencia falta = Asistencia.builder()
                .estudiante(estudiante)
                .curso(curso)
                .fecha(LocalDate.now())
                .tipo(TipoAsistencia.FALTA)
                .build();

        when(asistenciaRepository.save(any())).thenReturn(falta);
        when(asistenciaRepository.countByEstudianteIdAndCursoIdAndTipo(1L, 1L, TipoAsistencia.FALTA))
                .thenReturn(3L);
        when(notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(2L)).thenReturn(List.of());

        Asistencia resultado = asistenciaService.registrar(falta);

        assertEquals(TipoAsistencia.FALTA, resultado.getTipo());
        verify(asistenciaRepository).save(falta);
        verify(notificacionRepository).save(any());
    }

    @Test
    void deberia_no_duplicar_notificacion_si_ya_existe() {
        Asistencia falta = Asistencia.builder()
                .estudiante(estudiante)
                .curso(curso)
                .fecha(LocalDate.now())
                .tipo(TipoAsistencia.FALTA)
                .build();

        Notificacion existente = Notificacion.builder()
                .titulo("🔴 Pedro acumuló 3 faltas")
                .mensaje("Tu hijo Pedro García ya tiene 3 faltas en el curso. Por favor, conversa con él.")
                .build();

        when(asistenciaRepository.save(any())).thenReturn(falta);
        when(asistenciaRepository.countByEstudianteIdAndCursoIdAndTipo(1L, 1L, TipoAsistencia.FALTA))
                .thenReturn(3L);
        when(notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(2L))
                .thenReturn(List.of(existente));

        asistenciaService.registrar(falta);

        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void deberia_obtener_asistencia_por_id() {
        when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistencia));

        Asistencia resultado = asistenciaService.obtenerPorId(1L);

        assertNotNull(resultado);
    }

    @Test
    void deberia_listar_asistencias_por_estudiante_y_curso() {
        when(asistenciaRepository.findByEstudianteIdAndCursoId(1L, 1L)).thenReturn(List.of(asistencia));

        List<Asistencia> resultado = asistenciaService.listarPorEstudianteYCurso(1L, 1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_asistencias_por_curso_y_fecha() {
        LocalDate hoy = LocalDate.now();
        when(asistenciaRepository.findByCursoIdAndFecha(1L, hoy)).thenReturn(List.of(asistencia));

        List<Asistencia> resultado = asistenciaService.listarPorCursoYFecha(1L, hoy);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_contar_faltas() {
        when(asistenciaRepository.countByEstudianteIdAndCursoIdAndTipo(1L, 1L, TipoAsistencia.FALTA))
                .thenReturn(5L);

        long faltas = asistenciaService.contarFaltas(1L, 1L);

        assertEquals(5L, faltas);
    }

    @Test
    void deberia_eliminar_asistencia() {
        asistenciaService.eliminar(1L);
        verify(asistenciaRepository).deleteById(1L);
    }

    @Test
    void deberia_obtener_reporte_general() {
        when(asistenciaRepository.countByFiltros(null, null, TipoAsistencia.PRESENTE)).thenReturn(10L);
        when(asistenciaRepository.countByFiltros(null, null, TipoAsistencia.FALTA)).thenReturn(3L);
        when(asistenciaRepository.countByFiltros(null, null, TipoAsistencia.TARDANZA)).thenReturn(2L);
        when(asistenciaRepository.countByFiltros(null, null, TipoAsistencia.JUSTIFICADO)).thenReturn(1L);
        when(asistenciaRepository.findTopCursosConFaltas(null, null)).thenReturn(List.of());
        when(asistenciaRepository.findTopEstudiantesConFaltas(null, null)).thenReturn(List.of());

        ReporteAsistenciaDTO reporte = asistenciaService.obtenerReporteGeneral(null, null);

        assertEquals(62L, reporte.getAsistencia()); // (10/16)*100 = 62
        assertEquals(3L, reporte.getAusentes());
        assertEquals(2L, reporte.getTardanzas());
        assertEquals(1L, reporte.getJustificados());
    }
}
