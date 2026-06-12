package com.AcademicScope.service;

import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.model.Asistencia;
import com.AcademicScope.model.Notificacion;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.repository.AsistenciaRepository;
import com.AcademicScope.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
