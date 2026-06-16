package com.AcademicScope.service.recurso;

import com.AcademicScope.repository.recurso.RecursoRepository;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.model.Recurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.service.CloudinaryService; // Importación corregida según tu árbol de carpetas
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final CursoRepository cursoRepository;
    private final CloudinaryService cloudinaryService; // Servicio inyectado correctamente

    public Recurso crear(Recurso recurso) {
        recurso.setFechaSubida(LocalDateTime.now());
        return recursoRepository.save(recurso);
    }

    public Recurso subirRecurso(MultipartFile file, Long cursoId, String titulo, String tipo) {
        try {
            // 1. Buscamos el curso primero para asegurar que existe
            Curso curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            // 2. Subimos directamente a Cloudinary usando tu servicio
            String folderName = "AcademicScope/recursos";
            String cloudinaryUrl = cloudinaryService.uploadFile(file, folderName, tipo);

            // 3. Calculamos el tamaño legible del archivo
            String tamano = file.getSize() >= 1048576
                    ? String.format("%.1f MB", file.getSize() / 1048576.0)
                    : String.format("%.0f KB", file.getSize() / 1024.0);

            // 4. Construimos el Recurso asignando la URL en la nube
            Recurso recurso = Recurso.builder()
                    .curso(curso)
                    .titulo(titulo)
                    .tipo(tipo)
                    .url(cloudinaryUrl) // Se guarda la URL directa de internet (https://res.cloudinary.com...)
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
        recursoRepository.deleteById(id);
    }

    public String obtenerNombreArchivo(Long id) {
        Recurso recurso = obtenerPorId(id);
        return recurso.getTitulo();
    }
}