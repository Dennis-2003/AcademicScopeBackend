package com.AcademicScope.service.recurso;

import com.AcademicScope.repository.recurso.RecursoRepository;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.model.Recurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final CursoRepository cursoRepository;
    private final CloudinaryService cloudinaryService;

    public Recurso crear(Recurso recurso) {
        recurso.setFechaSubida(LocalDateTime.now());
        return recursoRepository.save(recurso);
    }

    public Recurso subirRecurso(MultipartFile file, Long cursoId, String titulo, String tipo) {
        try {
            Curso curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            String folderName = "AcademicScope/recursos";
            String cloudinaryUrl = cloudinaryService.uploadFile(file, folderName, tipo);

            String tamano = file.getSize() >= 1048576
                    ? String.format("%.1f MB", file.getSize() / 1048576.0)
                    : String.format("%.0f KB", file.getSize() / 1024.0);

            Recurso recurso = Recurso.builder()
                    .curso(curso)
                    .titulo(titulo)
                    .tipo(tipo)
                    .url(cloudinaryUrl)
                    .tamano(tamano)
                    .fechaSubida(LocalDateTime.now())
                    .build();

            return recursoRepository.save(recurso);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
        }
    }

    public List<Recurso> listarPorCurso(Long cursoId) {
        return recursoRepository.findByCursoId(cursoId);
    }

    public Recurso obtenerPorId(Long id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
    }

    public void eliminar(Long id) {
        Recurso recurso = obtenerPorId(id);
        cloudinaryService.eliminarArchivo(recurso.getUrl());
        recursoRepository.deleteById(id);
    }

    public String obtenerNombreArchivo(Long id) {
        Recurso recurso = obtenerPorId(id);
        return recurso.getTitulo();
    }
}

