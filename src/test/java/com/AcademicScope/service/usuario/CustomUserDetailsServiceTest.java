package com.AcademicScope.service.usuario;

import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .email("juan@test.com")
                .dni("12345678")
                .password("encodedPass")
                .nombre("Juan")
                .rol(RolUsuario.ADMIN)
                .activo(true)
                .build();
    }

    @Test
    void deberia_cargar_usuario_por_email() {
        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("juan@test.com");

        assertEquals("juan@test.com", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void deberia_cargar_usuario_por_dni_cuando_email_no_existe() {
        when(usuarioRepository.findByEmail("12345678")).thenReturn(Optional.empty());
        when(usuarioRepository.findByDni("12345678")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("12345678");

        assertNotNull(userDetails);
        assertEquals("juan@test.com", userDetails.getUsername());
    }

    @Test
    void deberia_lanzar_excepcion_cuando_usuario_no_existe() {
        when(usuarioRepository.findByEmail("no@existe.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByDni("no@existe.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("no@existe.com"));
    }

    @Test
    void deberia_asignar_rol_correcto_para_estudiante() {
        usuario.setRol(RolUsuario.ESTUDIANTE);
        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("juan@test.com");

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ESTUDIANTE")));
    }
}
