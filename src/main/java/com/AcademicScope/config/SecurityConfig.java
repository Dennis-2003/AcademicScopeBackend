package com.AcademicScope.config;

import com.AcademicScope.auth.filter.JwtAuthFilter;
import com.AcademicScope.service.usuario.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/usuarios/by-email/**").permitAll()
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
                .requestMatchers("/api/instituciones/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationProvider authenticationProvider() {
        org.springframework.security.authentication.dao.DaoAuthenticationProvider authProvider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @org.springframework.beans.factory.annotation.Value("${cors.allowed-origins:*}")
    private java.util.List<String> allowedOrigins;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        
        // Usamos setAllowedOriginPatterns para permitir credenciales y definir origenes especificos por entorno
        configuration.setAllowedOriginPatterns(allowedOrigins);
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);
        
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
