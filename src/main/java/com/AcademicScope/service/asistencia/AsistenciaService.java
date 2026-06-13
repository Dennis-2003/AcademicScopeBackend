package com.AcademicScope.service.asistencia;

import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.model.Asistencia;
import com.AcademicScope.enums.TipoAsistencia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.AcademicScope.dto.ReporteAsistenciaDTO;
import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.model.Notificacion;
import com.AcademicScope.repository.notificacion.NotificacionRepository;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final NotificacionRepository notificacionRepository;

    public Asistencia registrar(Asistencia asistencia) {
        Asistencia guardada = asistenciaRepository.save(asistencia);

        if (asistencia.getTipo() == TipoAsistencia.FALTA) {
            verificarFaltasYNotificar(asistencia.getEstudiante(), asistencia.getCurso().getId());
        }

        return guardada;
    }

    public Asistencia obtenerPorId(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
    }

    public List<Asistencia> listarPorEstudianteYCurso(Long estudianteId, Long cursoId) {
        return asistenciaRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId);
    }

    public List<Asistencia> listarPorCursoYFecha(Long cursoId, java.time.LocalDate fecha) {
        return asistenciaRepository.findByCursoIdAndFecha(cursoId, fecha);
    }

    public long contarFaltas(Long estudianteId, Long cursoId) {
        return asistenciaRepository.countByEstudianteIdAndCursoIdAndTipo(estudianteId, cursoId, TipoAsistencia.FALTA);
    }

    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

    public ReporteAsistenciaDTO obtenerReporteGeneral(Long gradoId, TipoCurso tipoCurso) {
        long presentes = asistenciaRepository.countByFiltros(gradoId, tipoCurso, TipoAsistencia.PRESENTE);
        long faltas = asistenciaRepository.countByFiltros(gradoId, tipoCurso, TipoAsistencia.FALTA);
        long tardanzas = asistenciaRepository.countByFiltros(gradoId, tipoCurso, TipoAsistencia.TARDANZA);
        long justificados = asistenciaRepository.countByFiltros(gradoId, tipoCurso, TipoAsistencia.JUSTIFICADO);

        long total = presentes + faltas + tardanzas + justificados;
        long porcentaje = total > 0 ? (presentes * 100) / total : 0;

        List<Object[]> topCursos = asistenciaRepository.findTopCursosConFaltas(gradoId, tipoCurso);
        List<ReporteAsistenciaDTO.CursoRiesgoDTO> cursosRiesgo = topCursos.stream()
                .limit(5)
                .map(obj -> {
                    Curso c = (Curso) obj[0];
                    long f = (long) obj[1];
                    return ReporteAsistenciaDTO.CursoRiesgoDTO.builder()
                            .curso(c.getNombre())
                            .grado(c.getGrado() != null ? c.getGrado().getNombre() : "N/A")
                            .ausencias(f)
                            .docente(c.getDocente() != null ? c.getDocente().getNombre() + " " + c.getDocente().getApellido() : "Sin asignar")
                            .build();
                }).collect(Collectors.toList());

        List<Object[]> topEstudiantes = asistenciaRepository.findTopEstudiantesConFaltas(gradoId, tipoCurso);
        List<ReporteAsistenciaDTO.EstudianteRiesgoDTO> estudiantesRiesgo = topEstudiantes.stream()
                .limit(5)
                .map(obj -> {
                    Usuario e = (Usuario) obj[0];
                    String g = (String) obj[1];
                    long f = (long) obj[2];
                    return ReporteAsistenciaDTO.EstudianteRiesgoDTO.builder()
                            .nombre(e.getNombre() + " " + e.getApellido())
                            .grado(g != null ? g : "N/A")
                            .faltas(f)
                            .build();
                }).collect(Collectors.toList());

        return ReporteAsistenciaDTO.builder()
                .asistencia(porcentaje)
                .ausentes(faltas)
                .tardanzas(tardanzas)
                .justificados(justificados)
                .cursosRiesgo(cursosRiesgo)
                .estudiantesRiesgo(estudiantesRiesgo)
                .build();
    }

    private void verificarFaltasYNotificar(Usuario estudiante, Long cursoId) {
        long faltas = contarFaltas(estudiante.getId(), cursoId);

        if (faltas >= 3) {
            Usuario tutor = estudiante.getTutor();
            if (tutor != null) {
                boolean yaNotificado = notificacionRepository
                        .findByUsuarioIdOrderByFechaEnvioDesc(tutor.getId())
                        .stream().anyMatch(n ->
                                n.getTitulo().contains("Faltas") &&
                                n.getMensaje().contains(estudiante.getNombre()));

                if (!yaNotificado) {
                    notificacionRepository.save(Notificacion.builder()
                            .usuario(tutor)
                            .titulo("🔴 " + estudiante.getNombre() + " acumuló " + faltas + " faltas")
                            .mensaje("Tu hijo " + estudiante.getNombre() + " " + estudiante.getApellido()
                                    + " ya tiene " + faltas + " faltas en el curso. Por favor, conversa con él.")
                            .leido(false)
                            .fechaEnvio(java.time.LocalDateTime.now())
                            .build());
                }
            }
        }
    }
}
