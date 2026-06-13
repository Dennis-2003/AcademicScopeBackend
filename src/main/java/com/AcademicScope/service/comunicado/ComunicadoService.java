package com.AcademicScope.service.comunicado;

import com.AcademicScope.model.Comunicado;
import com.AcademicScope.repository.comunicado.ComunicadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;
    private final String UPLOAD_DIR = "uploads/";

    public List<Comunicado> listarComunicados() {
        return comunicadoRepository.findAllByOrderByFechaDescIdDesc();
    }

    @Transactional
    public Comunicado crearComunicado(Comunicado comunicado, MultipartFile archivo) {
        if (comunicado.getFecha() == null) {
            comunicado.setFecha(LocalDate.now());
        }

        if (archivo != null && !archivo.isEmpty()) {
            try {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String originalName = archivo.getOriginalFilename();
                String extension = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
                String uniqueName = UUID.randomUUID().toString() + extension;
                
                Path filepath = Paths.get(UPLOAD_DIR, uniqueName);
                archivo.transferTo(filepath);

                comunicado.setArchivoNombre(originalName);
                comunicado.setArchivoUrl(uniqueName); // Guardamos solo el nombre único
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo", e);
            }
        }

        return comunicadoRepository.save(comunicado);
    }

    @Transactional
    public void eliminarComunicado(Long id) {
        comunicadoRepository.deleteById(id);
    }
}
