package com.AcademicScope;

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
import org.springframework.core.env.Environment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final GradoRepository gradoRepository;
    private final CursoRepository cursoRepository;
    private final InstitucionRepository institucionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    public void run(String... args) {
        crearInstitucionBase();
        crearUsuarios();
        crearGradosYCursos();
        System.out.println(" Inicialización completada");
    }

    private void crearInstitucionBase() {
        if (institucionRepository.findByRuc("10000000001").isEmpty()) {
            institucionRepository.save(Institucion.builder()
                    .nombreColegio("Colegio AcademicScope Por Defecto")
                    .ruc("10000000001")
                    .planSuscripcion("PREMIUM")
                    .build());
        }
    }

    private void crearUsuarios() {
        // Administradores del Equipo leídos desde el .env
        crearUsuarioSiNoExiste(env.getProperty("ADMIN1_EMAIL", "dennis@academicscope.com"), env.getProperty("ADMIN1_DNI", "70000001"), env.getProperty("ADMIN1_NOMBRE", "Dennis"), env.getProperty("ADMIN1_APELLIDO", "Admin"), env.getProperty("ADMIN1_PASSWORD", "Dennis@2026!"), RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(env.getProperty("ADMIN2_EMAIL", "massiel@academicscope.com"), env.getProperty("ADMIN2_DNI", "70000002"), env.getProperty("ADMIN2_NOMBRE", "Massiel"), env.getProperty("ADMIN2_APELLIDO", "Admin"), env.getProperty("ADMIN2_PASSWORD", "Massiel@2026!"), RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(env.getProperty("ADMIN3_EMAIL", "yadira@academicscope.com"), env.getProperty("ADMIN3_DNI", "70000003"), env.getProperty("ADMIN3_NOMBRE", "Yadira"), env.getProperty("ADMIN3_APELLIDO", "Admin"), env.getProperty("ADMIN3_PASSWORD", "Yadira@2026!"), RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(env.getProperty("ADMIN4_EMAIL", "edmar@academicscope.com"), env.getProperty("ADMIN4_DNI", "70000004"), env.getProperty("ADMIN4_NOMBRE", "Edmar"), env.getProperty("ADMIN4_APELLIDO", "Admin"), env.getProperty("ADMIN4_PASSWORD", "Edmar@2026!"), RolUsuario.ADMIN, null);

        // Docentes Generados (Mock) leídos desde el .env
        crearUsuarioSiNoExiste(env.getProperty("DOCENTE1_EMAIL", "profesor.mario@academicscope.com"), env.getProperty("DOCENTE1_DNI", "12345678"), env.getProperty("DOCENTE1_NOMBRE", "Mario"), env.getProperty("DOCENTE1_APELLIDO", "Gutierrez"), env.getProperty("DOCENTE1_PASSWORD", "Profe@2026!"), RolUsuario.DOCENTE, null);
        crearUsuarioSiNoExiste(env.getProperty("DOCENTE2_EMAIL", "profesora.laura@academicscope.com"), env.getProperty("DOCENTE2_DNI", "87654321"), env.getProperty("DOCENTE2_NOMBRE", "Laura"), env.getProperty("DOCENTE2_APELLIDO", "Condori"), env.getProperty("DOCENTE2_PASSWORD", "Profe@2026!"), RolUsuario.DOCENTE, null);
        crearUsuarioSiNoExiste(env.getProperty("DOCENTE3_EMAIL", "profesor.carlos@academicscope.com"), env.getProperty("DOCENTE3_DNI", "11223344"), env.getProperty("DOCENTE3_NOMBRE", "Carlos"), env.getProperty("DOCENTE3_APELLIDO", "Perez"), env.getProperty("DOCENTE3_PASSWORD", "Profe@2026!"), RolUsuario.DOCENTE, null);

        // Tutores Generados (Mock)
        crearUsuarioSiNoExiste(env.getProperty("TUTOR1_EMAIL", "tutor.juan@academicscope.com"), env.getProperty("TUTOR1_DNI", "33333333"), env.getProperty("TUTOR1_NOMBRE", "Juan"), env.getProperty("TUTOR1_APELLIDO", "Quispe"), env.getProperty("TUTOR1_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);
        crearUsuarioSiNoExiste(env.getProperty("TUTOR2_EMAIL", "tutora.ana@academicscope.com"), env.getProperty("TUTOR2_DNI", "44444444"), env.getProperty("TUTOR2_NOMBRE", "Ana"), env.getProperty("TUTOR2_APELLIDO", "Mamani"), env.getProperty("TUTOR2_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);
        crearUsuarioSiNoExiste(env.getProperty("TUTOR3_EMAIL", "tutor.luis@academicscope.com"), env.getProperty("TUTOR3_DNI", "55555555"), env.getProperty("TUTOR3_NOMBRE", "Luis"), env.getProperty("TUTOR3_APELLIDO", "Sanchez"), env.getProperty("TUTOR3_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);
        crearUsuarioSiNoExiste(env.getProperty("TUTOR4_EMAIL", "tutor.Smith@academicscope.com"), env.getProperty("TUTOR4_DNI", "71967699"), env.getProperty("TUTOR4_NOMBRE", "Smith"), env.getProperty("TUTOR4_APELLIDO", "Quiroz"), env.getProperty("TUTOR4_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);

        // Obtener a los tutores recién creados para asignarlos a los estudiantes
        Usuario tutor1 = usuarioRepository.findByDni(env.getProperty("TUTOR1_DNI", "33333333")).orElse(null);
        Usuario tutor2 = usuarioRepository.findByDni(env.getProperty("TUTOR2_DNI", "44444444")).orElse(null);
        Usuario tutor3 = usuarioRepository.findByDni(env.getProperty("TUTOR3_DNI", "55555555")).orElse(null);

        // Estudiantes Generados (Mock) asignados a sus respectivos tutores
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE1_EMAIL", "estudiante.pedro@academicscope.com"), env.getProperty("ESTUDIANTE1_DNI", "11111111"), env.getProperty("ESTUDIANTE1_NOMBRE", "Pedro"), env.getProperty("ESTUDIANTE1_APELLIDO", "Quispe"), env.getProperty("ESTUDIANTE1_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor1);
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE2_EMAIL", "estudiante.lucia@academicscope.com"), env.getProperty("ESTUDIANTE2_DNI", "22222222"), env.getProperty("ESTUDIANTE2_NOMBRE", "Lucia"), env.getProperty("ESTUDIANTE2_APELLIDO", "Quispe"), env.getProperty("ESTUDIANTE2_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor1);
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE3_EMAIL", "estudiante.marcos@academicscope.com"), env.getProperty("ESTUDIANTE3_DNI", "66666666"), env.getProperty("ESTUDIANTE3_NOMBRE", "Marcos"), env.getProperty("ESTUDIANTE3_APELLIDO", "Mamani"), env.getProperty("ESTUDIANTE3_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor2);
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE4_EMAIL", "estudiante.sofia@academicscope.com"), env.getProperty("ESTUDIANTE4_DNI", "77777777"), env.getProperty("ESTUDIANTE4_NOMBRE", "Sofia"), env.getProperty("ESTUDIANTE4_APELLIDO", "Sanchez"), env.getProperty("ESTUDIANTE4_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor3);
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
            System.out.println("=========================================");
            System.out.println(" NUEVO USUARIO CREADO: " + nombre + " " + apellido);
            System.out.println(" Rol: " + rol);
            System.out.println(" Email: " + email);
            System.out.println(" Password (desde .env o default): " + rawPassword);
            System.out.println("=========================================");
        }
    }

}
