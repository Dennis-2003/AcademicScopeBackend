package com.AcademicScope.initializer;

import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Grado;
import com.AcademicScope.model.Institucion;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.repository.academico.GradoRepository;
import com.AcademicScope.repository.institucion.InstitucionRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1) // Se ejecuta primero
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class BaseDataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final GradoRepository gradoRepository;
    private final CursoRepository cursoRepository;
    private final InstitucionRepository institucionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    public void run(String... args) {
        log.info("--- Iniciando carga de Datos Base ---");
        crearInstitucionBase();
        crearAdmins();
        crearGradosYCursos();
        log.info("--- Carga de Datos Base completada ---");
    }

    private void crearInstitucionBase() {
        if (institucionRepository.findByRuc("10000000001").isEmpty()) {
            institucionRepository.save(Institucion.builder()
                    .nombreColegio("Colegio AcademicScope Por Defecto")
                    .ruc("10000000001")
                    .planSuscripcion("PREMIUM")
                    .build());
            log.info("Institución base creada.");
        }
    }

    private void crearAdmins() {
        crearUsuarioSiNoExiste(env.getProperty("ADMIN1_EMAIL", "dennis@academicscope.com"), env.getProperty("ADMIN1_DNI", "70000001"), env.getProperty("ADMIN1_NOMBRE", "Dennis"), env.getProperty("ADMIN1_APELLIDO", "Admin"), env.getProperty("ADMIN1_PASSWORD", "Dennis@2026!"), RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(env.getProperty("ADMIN2_EMAIL", "massiel@academicscope.com"), env.getProperty("ADMIN2_DNI", "70000002"), env.getProperty("ADMIN2_NOMBRE", "Massiel"), env.getProperty("ADMIN2_APELLIDO", "Admin"), env.getProperty("ADMIN2_PASSWORD", "Massiel@2026!"), RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(env.getProperty("ADMIN3_EMAIL", "yadira@academicscope.com"), env.getProperty("ADMIN3_DNI", "70000003"), env.getProperty("ADMIN3_NOMBRE", "Yadira"), env.getProperty("ADMIN3_APELLIDO", "Admin"), env.getProperty("ADMIN3_PASSWORD", "Yadira@2026!"), RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(env.getProperty("ADMIN4_EMAIL", "edmar@academicscope.com"), env.getProperty("ADMIN4_DNI", "70000004"), env.getProperty("ADMIN4_NOMBRE", "Edmar"), env.getProperty("ADMIN4_APELLIDO", "Admin"), env.getProperty("ADMIN4_PASSWORD", "Edmar@2026!"), RolUsuario.ADMIN, null);
    }

    private void crearGradosYCursos() {
        List<String> niveles = Arrays.asList(
                "1° Primaria", "2° Primaria", "3° Primaria",
                "4° Primaria", "5° Primaria");

        List<String> cursosBase = Arrays.asList(
                "Matemáticas", "Comunicación", "Ciencia y Ambiente",
                "Personal Social", "Educación Física", "Arte y Cultura", "Inglés");

        for (String nombreGrado : niveles) {
            if (!gradoRepository.existsByNombre(nombreGrado)) {
                Grado grado = gradoRepository.save(
                        Grado.builder().nombre(nombreGrado).nivel("Primaria").build());

                for (String nombreCurso : cursosBase) {
                    String codigo = "CUR-" + nombreGrado.charAt(0) + "-" + nombreCurso.substring(0, 3).toUpperCase();
                    if (cursoRepository.findByGradoId(grado.getId()).stream()
                            .noneMatch(c -> c.getNombre().equals(nombreCurso))) {
                        cursoRepository.save(Curso.builder()
                                .nombre(nombreCurso)
                                .codigo(codigo)
                                .grado(grado)
                                .tipo(TipoCurso.REGULAR)
                                .build());
                    }
                }
            }
        }

        List<String> talleres = Arrays.asList("Música", "Deporte", "Danza", "Computación", "Manualidades");
        for (String taller : talleres) {
            if (cursoRepository.findByTipo(TipoCurso.TALLER).stream()
                    .noneMatch(t -> t.getNombre().equals(taller))) {
                cursoRepository.save(Curso.builder()
                        .nombre(taller)
                        .codigo("TAL-" + taller.substring(0, 3).toUpperCase())
                        .tipo(TipoCurso.TALLER)
                        .build());
            }
        }
        log.info("Grados y cursos base verificados/creados.");
    }

    private void crearUsuarioSiNoExiste(String email, String dni, String nombre,
                                         String apellido, String rawPassword, RolUsuario rol, Usuario tutor) {
        if (usuarioRepository.findByEmail(email).isEmpty() && usuarioRepository.findByDni(dni).isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .email(email)
                    .dni(dni)
                    .password(passwordEncoder.encode(rawPassword))
                    .rol(rol)
                    .tutor(tutor)
                    .activo(true)
                    .primerIngreso(true)
                    .build());
            log.info("Usuario Base creado: {} {} ({})", nombre, apellido, rol);
        }
    }
}
