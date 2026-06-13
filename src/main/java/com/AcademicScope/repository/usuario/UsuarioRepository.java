package com.AcademicScope.repository.usuario;

import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    List<Usuario> findByRol(RolUsuario rol);
}
