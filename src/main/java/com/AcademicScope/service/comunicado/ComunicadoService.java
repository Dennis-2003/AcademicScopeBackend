package com.AcademicScope.service.comunicado;

import com.AcademicScope.model.Comunicado;
import com.AcademicScope.repository.comunicado.ComunicadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;

    private final com.AcademicScope.service.CloudinaryService cloudinaryService;

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
                String originalName = archivo.getOriginalFilename();
                String fileUrl = cloudinaryService.uploadFile(archivo, "academicscope/comunicados", "auto");

                comunicado.setArchivoNombre(originalName);
                comunicado.setArchivoUrl(fileUrl); // Guardamos la URL pública de Cloudinary
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo en Cloudinary", e);
            }
        }

        return comunicadoRepository.save(comunicado);
    }

    @Transactional
    public void eliminarComunicado(Long id) {
        comunicadoRepository.deleteById(id);
    }
}

