package com.AcademicScope.service.dashboard;

import com.AcademicScope.dto.DashboardRendimientoDTO;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.model.Asistencia;
import com.AcademicScope.model.Curso;
import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.repository.academico.CursoRepository;
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
}
