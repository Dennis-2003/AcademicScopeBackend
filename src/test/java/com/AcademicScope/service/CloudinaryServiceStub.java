package com.AcademicScope.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class CloudinaryServiceStub extends CloudinaryService {

    public CloudinaryServiceStub() {
        super(null);
    }

    @Override
    public String uploadFile(MultipartFile file, String folder, String resourceType) throws IOException {
        return "https://stub.cloudinary.com/" + resourceType + "/" + folder + "/" + file.getOriginalFilename();
    }
}
