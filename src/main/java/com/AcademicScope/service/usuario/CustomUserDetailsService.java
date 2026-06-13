package com.AcademicScope.service.usuario;

import com.AcademicScope.repository.usuario.UsuarioRepository;
import com.AcademicScope.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseGet(() -> usuarioRepository.findByDni(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email/DNI: " + username)));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getActivo(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
