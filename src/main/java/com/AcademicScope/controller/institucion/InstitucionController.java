package com.AcademicScope.controller.institucion;

import com.AcademicScope.repository.institucion.InstitucionRepository;
import com.AcademicScope.model.Institucion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instituciones")
@RequiredArgsConstructor
public class InstitucionController {

    private final InstitucionRepository institucionRepository;

    @GetMapping("/me")
    public ResponseEntity<Institucion> getMiInstitucion() {
        return institucionRepository.findAll().stream().findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<Institucion> updateMiInstitucion(@RequestBody Institucion institucionDetails) {
        return institucionRepository.findAll().stream().findFirst().map(institucion -> {
            institucion.setNombreColegio(institucionDetails.getNombreColegio());
            institucion.setLogoUrl(institucionDetails.getLogoUrl());
            // No actualizamos el RUC o ID por seguridad
            return ResponseEntity.ok(institucionRepository.save(institucion));
        }).orElse(ResponseEntity.notFound().build());
    }
}
