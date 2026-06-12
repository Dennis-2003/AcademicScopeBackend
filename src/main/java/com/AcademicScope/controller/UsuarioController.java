package com.AcademicScope.controller;

import com.AcademicScope.dto.CambioPasswordDTO;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}
