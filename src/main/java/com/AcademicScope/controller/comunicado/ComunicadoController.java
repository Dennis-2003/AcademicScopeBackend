package com.AcademicScope.controller.comunicado;

import com.AcademicScope.model.Comunicado;
import com.AcademicScope.service.comunicado.ComunicadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/comunicados")
@RequiredArgsConstructor
public class ComunicadoController {

    private final ComunicadoService comunicadoService;
    private final ObjectMapper objectMapper;

    @org.springframework.beans.factory.annotation.Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<List<Comunicado>> listarComunicados() {
        return ResponseEntity.ok(comunicadoService.listarComunicados());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Comunicado> crearComunicado(
            @RequestPart("comunicado") String comunicadoJson,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) throws Exception {
        
        Comunicado comunicado = objectMapper.readValue(comunicadoJson, Comunicado.class);
        return ResponseEntity.ok(comunicadoService.crearComunicado(comunicado, archivo));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se pudo leer el archivo!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComunicado(@PathVariable Long id) {
        comunicadoService.eliminarComunicado(id);
        return ResponseEntity.noContent().build();
    }
}
