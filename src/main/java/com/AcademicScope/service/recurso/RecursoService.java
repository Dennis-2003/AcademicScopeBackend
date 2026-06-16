package com.AcademicScope.service.recurso;

import com.AcademicScope.repository.recurso.RecursoRepository;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.service.CloudinaryService;
import com.AcademicScope.model.Recurso;
import com.AcademicScope.model.Curso;
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
    private final CloudinaryService cloudinaryService;

    public Recurso crear(Recurso recurso) {
        recurso.setFechaSubida(LocalDateTime.now());
        return recursoRepository.save(recurso);
    }

    public Recurso subirRecurso(MultipartFile file, Long cursoId, String titulo, String tipo) {
        try {
            String tipoCloudinary = tipo.equalsIgnoreCase("PDF") ? "raw" : "auto";
            String url = cloudinaryService.uploadFile(file, "academicscope/recursos", tipoCloudinary);
            Curso curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            String tamano = file.getSize() >= 1048576
                ? String.format("%.1f MB", file.getSize() / 1048576.0)
                : String.format("%.0f KB", file.getSize() / 1024.0);

            Recurso recurso = Recurso.builder()
                    .curso(curso)
                    .titulo(titulo)
                    .tipo(tipo)
                    .url(url)
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

    public void eliminar(Long id) {
        recursoRepository.deleteById(id);
    }

    public Recurso obtenerPorId(Long id) {
        return recursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
    }
}
