package com.AcademicScope.service.recurso;

import com.AcademicScope.repository.recurso.RecursoRepository;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Recurso;
import com.AcademicScope.repository.academico.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final CursoRepository cursoRepository;
    private final CloudinaryService cloudinaryService;

    public Recurso crear(Recurso recurso) {
        recurso.setFechaSubida(LocalDateTime.now());
        return recursoRepository.save(recurso);
    }

    public Recurso subirArchivo(MultipartFile file, Long cursoId, String titulo, String tipo) {
        String url = cloudinaryService.subirArchivo(file);

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        Recurso recurso = new Recurso();
        recurso.setCurso(curso);
        recurso.setTitulo(titulo);
        recurso.setTipo(tipo);
        recurso.setUrl(url);
        recurso.setTamano(formatearTamano(file.getSize()));
        recurso.setFechaSubida(LocalDateTime.now());

        return recursoRepository.save(recurso);
    }

    private String formatearTamano(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }

    public List<Recurso> listarPorCurso(Long cursoId) {
        return recursoRepository.findByCursoId(cursoId);
    }

    public void eliminar(Long id) {
        Recurso recurso = obtenerPorId(id);
        cloudinaryService.eliminarArchivo(recurso.getUrl());
        recursoRepository.deleteById(id);
    }

    public Recurso obtenerPorId(Long id) {
        return recursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
    }
}
