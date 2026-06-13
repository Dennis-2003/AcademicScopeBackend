package com.AcademicScope.service.academico;

import com.AcademicScope.repository.academico.PeriodoAcademicoRepository;
import com.AcademicScope.model.PeriodoAcademico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodoAcademicoService {

    private final PeriodoAcademicoRepository periodoRepository;

    public PeriodoAcademico crear(PeriodoAcademico periodo) {
        return periodoRepository.save(periodo);
    }

    public PeriodoAcademico actualizar(Long id, PeriodoAcademico periodo) {
        return periodoRepository.findById(id).map(p -> {
            p.setNombre(periodo.getNombre());
            p.setFechaInicio(periodo.getFechaInicio());
            p.setFechaFin(periodo.getFechaFin());
            p.setActivo(periodo.getActivo());
            return periodoRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Periodo no encontrado"));
    }

    public PeriodoAcademico obtenerPorId(Long id) {
        return periodoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado"));
    }

    public List<PeriodoAcademico> listarTodos() {
        return periodoRepository.findAll();
    }

    public PeriodoAcademico obtenerActivo() {
        return periodoRepository.findByActivoTrue()
                .orElseThrow(() -> new RuntimeException("No hay periodo activo"));
    }

    public void eliminar(Long id) {
        periodoRepository.deleteById(id);
    }
}
