package com.AcademicScope.service.dashboard;

import com.AcademicScope.dto.DashboardRendimientoDTO;
import com.AcademicScope.dto.TutorDashboardDTO;
import com.AcademicScope.dto.ResumenHijoDTO;
import com.AcademicScope.dto.AlertaDTO;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.model.Asistencia;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.model.Matricula;
import com.AcademicScope.model.Calificacion;
import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import com.AcademicScope.repository.matricula.MatriculaRepository;
import com.AcademicScope.repository.evaluacion.CalificacionRepository;
import com.AcademicScope.repository.notificacion.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CursoRepository cursoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;
    private final CalificacionRepository calificacionRepository;
    private final NotificacionRepository notificacionRepository;

    public DashboardRendimientoDTO obtenerRendimiento() {
        List<String> meses = new ArrayList<>();
        List<Integer> cursosActivos = new ArrayList<>();
        List<Integer> asistenciaMedia = new ArrayList<>();

        LocalDate now = LocalDate.now();
        List<Curso> todosCursos = cursoRepository.findAll();
        List<Asistencia> todasAsistencias = asistenciaRepository.findAll();

        // Calculate for the last 7 months including current
        for (int i = 6; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            
            // Format month name (e.g., Ene, Feb, Mar)
            String monthName = monthDate.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
            // Quick hack for Spanish: make sure Ene, Feb, Mar, Abr, May, Jun, Jul is handled cleanly
            if (monthName.length() > 3) monthName = monthName.substring(0, 3);
            meses.add(monthName);

            // Active courses up to this month
            long countCursos = todosCursos.stream()
                .filter(c -> c.getFechaCreacion() != null && 
                             !c.getFechaCreacion().isAfter(monthDate.withDayOfMonth(monthDate.lengthOfMonth())))
                .count();
            
            // For legacy courses without a creation date, we assume they were always active.
            long nullDateCursos = todosCursos.stream().filter(c -> c.getFechaCreacion() == null).count();
            
            cursosActivos.add((int) (countCursos + nullDateCursos));

            // Average attendance for this month
            List<Asistencia> asistenciasDelMes = todasAsistencias.stream()
                .filter(a -> a.getFecha() != null &&
                             a.getFecha().getYear() == monthDate.getYear() &&
                             a.getFecha().getMonth() == monthDate.getMonth())
                .collect(Collectors.toList());

            if (asistenciasDelMes.isEmpty()) {
                asistenciaMedia.add(0); // If no data, return 0 or maybe carry over
            } else {
                long presentes = asistenciasDelMes.stream()
                    .filter(a -> a.getTipo() == TipoAsistencia.PRESENTE || a.getTipo() == TipoAsistencia.TARDANZA)
                    .count();
                int pct = (int) ((presentes * 100) / asistenciasDelMes.size());
                asistenciaMedia.add(pct);
            }
        }

        return DashboardRendimientoDTO.builder()
                .meses(meses)
                .cursosActivos(cursosActivos)
                .asistenciaMedia(asistenciaMedia)
                .build();
    }

    public TutorDashboardDTO obtenerDashboardTutor(Long tutorId) {
        List<Usuario> hijos = usuarioRepository.findByTutorId(tutorId);
        int notificaciones = notificacionRepository.findByUsuarioIdAndLeidoFalse(tutorId).size();
        
        List<ResumenHijoDTO> resumenHijos = new ArrayList<>();
        List<AlertaDTO> alertas = new ArrayList<>();

        for (Usuario hijo : hijos) {
            String grado = "Sin matrícula";
            String semaforo = "GRIS";
            int asistenciaTotal = 100;

            // Calificaciones para semáforo y alertas
            List<Calificacion> notas = calificacionRepository.findByEstudianteId(hijo.getId());
            if (!notas.isEmpty()) {
                double suma = notas.stream().mapToDouble(Calificacion::getNota).sum();
                long notaMedia = Math.round(suma / notas.size());
                
                if (notaMedia >= 14) semaforo = "VERDE";
                else if (notaMedia >= 11) semaforo = "AMBAR";
                else semaforo = "ROJO";

                // Generar alertas por notas bajas
                for (Calificacion nota : notas) {
                    if (nota.getNota() < 14) {
                        String cursoNombre = nota.getEvaluacion() != null && nota.getEvaluacion().getCurso() != null 
                            ? nota.getEvaluacion().getCurso().getNombre() : "Curso";
                        
                        alertas.add(AlertaDTO.builder()
                            .id("nota-" + nota.getId())
                            .tipo(nota.getNota() < 11 ? "ROJO" : "AMBAR")
                            .titulo("Bajo rendimiento en " + cursoNombre)
                            .mensaje(hijo.getNombre() + " ha obtenido una nota de " + nota.getNota() + " en " + (nota.getEvaluacion() != null ? nota.getEvaluacion().getNombre() : "una evaluación") + ".")
                            .build());
                    }
                }
            }

            // Matrículas y asistencia
            List<Matricula> matriculas = matriculaRepository.findByEstudianteId(hijo.getId()).stream()
                .filter(m -> m.getEstado() != null && !m.getEstado().name().equals("RETIRADA"))
                .collect(Collectors.toList());

            if (!matriculas.isEmpty() && matriculas.get(0).getGrado() != null) {
                grado = matriculas.get(0).getGrado().getNombre();
            }

            long totalAsistencias = 0;
            long totalAsistidos = 0;

            for (Matricula m : matriculas) {
                if (m.getGrado() != null && m.getGrado().getCursos() != null) {
                    for (Curso curso : m.getGrado().getCursos()) {
                        List<Asistencia> asistenciasCurso = asistenciaRepository.findByEstudianteIdAndCursoId(hijo.getId(), curso.getId());
                        totalAsistencias += asistenciasCurso.size();
                        totalAsistidos += asistenciasCurso.stream()
                            .filter(a -> a.getTipo() == TipoAsistencia.PRESENTE || a.getTipo() == TipoAsistencia.TARDANZA || a.getTipo().name().equals("JUSTIFICADO"))
                            .count();
                    }
                }
            }

            if (totalAsistencias > 0) {
                asistenciaTotal = (int) Math.round((double) totalAsistidos / totalAsistencias * 100);
            }

            if (asistenciaTotal < 90) {
                alertas.add(AlertaDTO.builder()
                    .id("asis-" + hijo.getId())
                    .tipo("AMBAR")
                    .titulo("Baja asistencia global")
                    .mensaje(hijo.getNombre() + " tiene una asistencia del " + asistenciaTotal + "%")
                    .build());
            }

            resumenHijos.add(ResumenHijoDTO.builder()
                .id(hijo.getId())
                .nombre(hijo.getNombre() + " " + hijo.getApellido())
                .grado(grado)
                .semaforo(semaforo)
                .asistencia(asistenciaTotal)
                .build());
        }

        // Ordenar alertas para que ROJO vaya primero y luego limitar a 5
        alertas.sort((a, b) -> a.getTipo().compareTo(b.getTipo())); // "AMBAR" vs "ROJO" - actually R comes after A.
        // Wait, "ROJO" > "AMBAR". We want ROJO first.
        alertas.sort((a, b) -> b.getTipo().compareTo(a.getTipo()));
        
        List<AlertaDTO> ultimasAlertas = alertas.stream().limit(5).collect(Collectors.toList());

        return TutorDashboardDTO.builder()
            .hijos(hijos.size())
            .notificaciones(notificaciones)
            .resumenHijos(resumenHijos)
            .alertas(ultimasAlertas)
            .build();
    }
}
