package com.AcademicScope.auth.service;

import com.AcademicScope.auth.dto.AuthResponse;
import com.AcademicScope.auth.dto.LoginRequest;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        Usuario usuario = usuarioRepository.findByEmail(request.getUsername())
                .orElseGet(() -> usuarioRepository.findByDni(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .dni(usuario.getDni())
                .rol(usuario.getRol())
                .primerIngreso(usuario.getPrimerIngreso())
                .build();
    }
}
