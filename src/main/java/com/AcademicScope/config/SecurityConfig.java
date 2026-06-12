package com.AcademicScope.config;

import com.AcademicScope.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/usuarios/cambiar-password").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/grados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/cursos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/cursos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/cursos/**").hasRole("ADMIN")
                .requestMatchers("/api/periodos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/evaluaciones/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/calificaciones/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/comportamientos/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/notificaciones/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/asistencias/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.GET, "/api/asistencias/**").hasAnyRole("ESTUDIANTE", "TUTOR", "ADMIN", "DOCENTE")
                .requestMatchers("/api/calificaciones/estudiante/**").hasAnyRole("ESTUDIANTE", "TUTOR", "ADMIN")
                .requestMatchers("/api/comportamientos/estudiante/**").hasAnyRole("ESTUDIANTE", "TUTOR", "ADMIN")
                .requestMatchers("/api/notificaciones/usuario/**").authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint((request, response, authException) -> response.sendError(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())))
            .userDetailsService(userDetailsService);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
