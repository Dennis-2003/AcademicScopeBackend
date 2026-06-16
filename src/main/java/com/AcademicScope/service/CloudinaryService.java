package com.AcademicScope.service;

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

    public String uploadFile(MultipartFile file, String folder, String resourceType) throws IOException {
        String cloudinaryResourceType = "auto";
        if ("PDF".equalsIgnoreCase(resourceType)) {
            cloudinaryResourceType = "raw";
        } else if ("VIDEO".equalsIgnoreCase(resourceType)) {
            cloudinaryResourceType = "video";
        } else if ("IMAGEN".equalsIgnoreCase(resourceType) || "IMAGE".equalsIgnoreCase(resourceType)) {
            cloudinaryResourceType = "image";
        }

        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("resource_type", cloudinaryResourceType);
        params.put("folder", folder);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.isEmpty()) {
            String baseName = originalFilename;
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                baseName = originalFilename.substring(0, dotIndex);
                extension = originalFilename.substring(dotIndex);
            }
            // Limpiar el nombre base de caracteres problemáticos
            baseName = baseName.replaceAll("[^a-zA-Z0-9-_]", "");
            if (baseName.isEmpty()) baseName = "file";
            
            String publicId = baseName + "_" + java.util.UUID.randomUUID().toString().substring(0, 8) + extension;
            params.put("public_id", publicId);
        }

        java.util.Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
        return uploadResult.get("secure_url").toString();
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}