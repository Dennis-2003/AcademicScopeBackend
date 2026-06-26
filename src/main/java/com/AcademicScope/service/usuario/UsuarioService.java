package com.AcademicScope.service.usuario;

import com.AcademicScope.repository.usuario.UsuarioRepository;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public Usuario crear(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getDni()));
        usuario.setPrimerIngreso(true);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(usuario.getNombre());
            u.setApellido(usuario.getApellido());
            u.setEmail(usuario.getEmail());
            u.setDni(usuario.getDni());
            u.setTelefono(usuario.getTelefono());
            u.setDireccion(usuario.getDireccion());
            u.setRol(usuario.getRol());
            u.setActivo(usuario.getActivo());
            u.setTutor(usuario.getTutor());
            return usuarioRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario obtenerPorDni(String dni) {
        return usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ese email"));
    }

    public Usuario subirAvatar(Long id, MultipartFile archivo) {
        Usuario usuario = obtenerPorId(id);
        if (archivo != null && !archivo.isEmpty()) {
            try {
                String avatarUrl = cloudinaryService.uploadFile(archivo, "academicscope/avatars", "auto");
                usuario.setAvatarUrl(avatarUrl);
                return usuarioRepository.save(usuario);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el avatar en Cloudinary", e);
            }
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPorRol(RolUsuario rol) {
        return usuarioRepository.findByRol(rol);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void cambiarPassword(String dni, String passwordActual, String passwordNuevo) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        PasswordValidator.validate(passwordNuevo);

        usuario.setPassword(passwordEncoder.encode(passwordNuevo));
        usuario.setPrimerIngreso(false);
        usuarioRepository.save(usuario);
    }
}

