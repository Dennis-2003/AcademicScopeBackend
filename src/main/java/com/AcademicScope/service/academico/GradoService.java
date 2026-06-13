package com.AcademicScope.service.academico;

import com.AcademicScope.repository.academico.GradoRepository;
import com.AcademicScope.model.Grado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradoService {

    private final GradoRepository gradoRepository;

    public Grado crear(Grado grado) {
        return gradoRepository.save(grado);
    }

    public Grado actualizar(Long id, Grado grado) {
        return gradoRepository.findById(id).map(g -> {
            g.setNombre(grado.getNombre());
            g.setNivel(grado.getNivel());
            return gradoRepository.save(g);
        }).orElseThrow(() -> new RuntimeException("Grado no encontrado"));
    }

    public Grado obtenerPorId(Long id) {
        return gradoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
    }

    public List<Grado> listarTodos() {
        return gradoRepository.findAll();
    }

    public List<Grado> listarPorNivel(String nivel) {
        return gradoRepository.findByNivel(nivel);
    }

    public void eliminar(Long id) {
        gradoRepository.deleteById(id);
    }
}
