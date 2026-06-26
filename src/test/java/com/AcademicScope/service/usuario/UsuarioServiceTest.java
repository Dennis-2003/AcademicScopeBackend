package com.AcademicScope.service.usuario;

import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UsuarioService usuarioService;
    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@test.com")
                .dni("12345678")
                .password("encoded")
                .rol(RolUsuario.ESTUDIANTE)
                .activo(true)
                .primerIngreso(true)
                .build();
    }

    @Test
    void deberia_crear_usuario_con_password_codificado_y_primer_ingreso() {
        when(passwordEncoder.encode("12345678")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.crear(usuario);

        assertTrue(resultado.getPrimerIngreso());
        assertEquals("encodedPassword", resultado.getPassword());
        verify(passwordEncoder).encode("12345678");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deberia_actualizar_usuario_existente() {
        Usuario actualizado = Usuario.builder()
                .nombre("Juan Carlos")
                .apellido("Pérez García")
                .email("juanc@test.com")
                .dni("87654321")
                .telefono("999888777")
                .direccion("Av. Siempre Viva")
                .rol(RolUsuario.DOCENTE)
                .activo(false)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.actualizar(1L, actualizado);

        assertEquals("Juan Carlos", resultado.getNombre());
        assertEquals("Pérez García", resultado.getApellido());
        assertEquals("juanc@test.com", resultado.getEmail());
        assertEquals("87654321", resultado.getDni());
        assertEquals("999888777", resultado.getTelefono());
        assertEquals(RolUsuario.DOCENTE, resultado.getRol());
        assertFalse(resultado.getActivo());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deberia_lanzar_excepcion_al_actualizar_usuario_inexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizar(99L, usuario));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void deberia_obtener_usuario_por_id() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerPorId(1L);

        assertEquals("Juan", resultado.getNombre());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void deberia_lanzar_excepcion_al_obtener_usuario_por_id_inexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.obtenerPorId(99L));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void deberia_obtener_usuario_por_dni() {
        when(usuarioRepository.findByDni("12345678")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerPorDni("12345678");

        assertEquals("juan@test.com", resultado.getEmail());
        verify(usuarioRepository).findByDni("12345678");
    }

    @Test
    void deberia_lanzar_excepcion_al_obtener_usuario_por_dni_inexistente() {
        when(usuarioRepository.findByDni("00000000")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.obtenerPorDni("00000000"));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void deberia_listar_todos_los_usuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void deberia_listar_usuarios_por_rol() {
        when(usuarioRepository.findByRol(RolUsuario.DOCENTE)).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarPorRol(RolUsuario.DOCENTE);

        assertEquals(1, resultado.size());
        verify(usuarioRepository).findByRol(RolUsuario.DOCENTE);
    }

    @Test
    void deberia_eliminar_usuario() {
        usuarioService.eliminar(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void deberia_cambiar_password_correctamente() {
        when(usuarioRepository.findByDni("12345678")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("actual", "encoded")).thenReturn(true);
        when(passwordEncoder.encode("Nueva@2026!")).thenReturn("nuevaEncoded");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        usuarioService.cambiarPassword("12345678", "actual", "Nueva@2026!");

        verify(usuarioRepository).findByDni("12345678");
        verify(passwordEncoder).matches("actual", "encoded");
        verify(passwordEncoder).encode("Nueva@2026!");
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertFalse(usuarioCaptor.getValue().getPrimerIngreso());
        assertEquals("nuevaEncoded", usuarioCaptor.getValue().getPassword());
    }

    @Test
    void deberia_lanzar_excepcion_si_password_actual_incorrecta() {
        when(usuarioRepository.findByDni("12345678")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("incorrecta", "encoded")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.cambiarPassword("12345678", "incorrecta", "Nueva@2026!"));
        assertEquals("Contraseña actual incorrecta", ex.getMessage());
    }

    @Test
    void deberia_lanzar_excepcion_si_password_nueva_no_cumple_requisitos() {
        when(usuarioRepository.findByDni("12345678")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("actual", "encoded")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.cambiarPassword("12345678", "actual", "corta"));

        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@ ! . _ -)", ex.getMessage());
    }
}
