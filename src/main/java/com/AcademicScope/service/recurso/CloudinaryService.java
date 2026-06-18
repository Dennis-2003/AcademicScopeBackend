package com.AcademicScope.service.recurso;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String subirArchivo(MultipartFile file) {
        try {
            Map<String, Object> params = ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "academicscope/recursos"
            );
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a Cloudinary: " + e.getMessage());
        }
    }

    public void eliminarArchivo(String url) {
        try {
            if (url == null || url.isEmpty()) return;
            String publicId = extraerPublicId(url);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar archivo de Cloudinary: " + e.getMessage());
        }
    }

    private String extraerPublicId(String url) {
        if (!url.contains("academicscope/recursos/")) return null;
        String[] parts = url.split("academicscope/recursos/");
        if (parts.length < 2) return null;
        String filename = parts[1];
        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        return "academicscope/recursos/" + filename;
    }
}
