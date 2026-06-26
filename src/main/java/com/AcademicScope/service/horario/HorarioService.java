package com.AcademicScope.service.horario;

import com.AcademicScope.repository.horario.HorarioRepository;
import com.AcademicScope.model.Horario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public Horario crear(Horario horario) {
        return horarioRepository.save(horario);
    }

    public List<Horario> listarPorCurso(Long cursoId) {
        return horarioRepository.findByCursoId(cursoId);
    }

    public List<Horario> listarPorDocente(Long docenteId) {
        return horarioRepository.findByCursoDocenteId(docenteId);
    }

    public List<Horario> listarTodos() {
        return horarioRepository.findAll();
    }

    public void eliminar(Long id) {
        horarioRepository.deleteById(id);
    }

    public Horario obtenerPorId(Long id) {
        return horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
    }

    public Horario actualizar(Long id, Horario horarioActualizado) {
        Horario existente = obtenerPorId(id);
        if (horarioActualizado.getDiaSemana() != null) {
            existente.setDiaSemana(horarioActualizado.getDiaSemana());
        }
        if (horarioActualizado.getHoraInicio() != null) {
            existente.setHoraInicio(horarioActualizado.getHoraInicio());
        }
        if (horarioActualizado.getHoraFin() != null) {
            existente.setHoraFin(horarioActualizado.getHoraFin());
        }
        if (horarioActualizado.getAula() != null) {
            existente.setAula(horarioActualizado.getAula());
        }
        return horarioRepository.save(existente);
    }
}

