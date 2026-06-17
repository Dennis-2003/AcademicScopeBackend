package com.AcademicScope.controller.usuario;

import com.AcademicScope.service.usuario.UsuarioService;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.dto.CambioPasswordDTO;
import com.AcademicScope.enums.RolUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.crear(usuario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication auth) {
        return ResponseEntity.ok(usuarioService.obtenerPorDni(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/by-email/{email:.+}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> listarPorRol(@PathVariable RolUsuario rol) {
        return ResponseEntity.ok(usuarioService.listarPorRol(rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(@RequestBody CambioPasswordDTO dto) {
        usuarioService.cambiarPassword(dto.getDni(), dto.getPasswordActual(), dto.getPasswordNuevo());
        return ResponseEntity.ok("Contraseña cambiada correctamente");
    }

    @PostMapping(value = "/{id}/avatar", consumes = {"multipart/form-data"})
    public ResponseEntity<Usuario> subirAvatar(@PathVariable Long id, @RequestPart("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(usuarioService.subirAvatar(id, archivo));
    }

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<Resource> obtenerAvatar(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/avatars/").resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se pudo leer el avatar!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
