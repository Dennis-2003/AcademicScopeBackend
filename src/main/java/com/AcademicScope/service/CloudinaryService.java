package com.AcademicScope.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folder, String resourceType) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String uniqueName = UUID.randomUUID().toString();

        Map<String, Object> params = ObjectUtils.asMap(
                "folder", folder,
                "public_id", uniqueName,
                "resource_type", resourceType
        );

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
        
        // Retornar la URL segura
        return uploadResult.get("secure_url").toString();
    }
}
