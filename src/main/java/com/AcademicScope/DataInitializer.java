package com.AcademicScope;

import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Grado;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.repository.CursoRepository;
import com.AcademicScope.repository.GradoRepository;
import com.AcademicScope.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:admin@academicscope.com}") private String adminEmail;
    @Value("${ADMIN_DNI:00000001}") private String adminDni;
    @Value("${ADMIN_NOMBRE:Admin}") private String adminNombre;
    @Value("${ADMIN_APELLIDO:Sistema}") private String adminApellido;

    @Value("${DOCENTE1_EMAIL:profesor.mario@academicscope.com}") private String d1Email;
    @Value("${DOCENTE1_DNI:12345678}") private String d1Dni;
    @Value("${DOCENTE1_NOMBRE:Mario}") private String d1Nombre;
    @Value("${DOCENTE1_APELLIDO:Gutierrez}") private String d1Apellido;

    @Value("${DOCENTE2_EMAIL:profesora.laura@academicscope.com}") private String d2Email;
    @Value("${DOCENTE2_DNI:87654321}") private String d2Dni;
    @Value("${DOCENTE2_NOMBRE:Laura}") private String d2Nombre;
    @Value("${DOCENTE2_APELLIDO:Condori}") private String d2Apellido;

    @Value("${ESTUDIANTE1_EMAIL:juan.quispe@academicscope.com}") private String e1Email;
    @Value("${ESTUDIANTE1_DNI:11111111}") private String e1Dni;
    @Value("${ESTUDIANTE1_NOMBRE:Juan}") private String e1Nombre;
    @Value("${ESTUDIANTE1_APELLIDO:Quispe}") private String e1Apellido;

    @Value("${ESTUDIANTE2_EMAIL:maria.huaman@academicscope.com}") private String e2Email;
    @Value("${ESTUDIANTE2_DNI:22222222}") private String e2Dni;
    @Value("${ESTUDIANTE2_NOMBRE:Maria}") private String e2Nombre;
    @Value("${ESTUDIANTE2_APELLIDO:Huaman}") private String e2Apellido;

    @Value("${TUTOR1_EMAIL:carlos.quispe@academicscope.com}") private String t1Email;
    @Value("${TUTOR1_DNI:33333333}") private String t1Dni;
    @Value("${TUTOR1_NOMBRE:Carlos}") private String t1Nombre;
    @Value("${TUTOR1_APELLIDO:Quispe}") private String t1Apellido;

    @Value("${TUTOR2_EMAIL:juana.condori@academicscope.com}") private String t2Email;
    @Value("${TUTOR2_DNI:44444444}") private String t2Dni;
    @Value("${TUTOR2_NOMBRE:Juana}") private String t2Nombre;
    @Value("${TUTOR2_APELLIDO:Condori}") private String t2Apellido;

    @Override
    public void run(String... args) {
        crearUsuarios();
        // crearGradosYCursos(); // Comentado para evitar que el sistema invente cursos automáticamente
        System.out.println(" Inicialización de usuarios base completada");
    }

    private void crearUsuarios() {
        crearUsuarioSiNoExiste(adminEmail, adminDni, adminNombre, adminApellido, RolUsuario.ADMIN, null);
        crearUsuarioSiNoExiste(d1Email, d1Dni, d1Nombre, d1Apellido, RolUsuario.DOCENTE, null);
        crearUsuarioSiNoExiste(d2Email, d2Dni, d2Nombre, d2Apellido, RolUsuario.DOCENTE, null);
        crearUsuarioSiNoExiste(t1Email, t1Dni, t1Nombre, t1Apellido, RolUsuario.TUTOR, null);
        crearUsuarioSiNoExiste(t2Email, t2Dni, t2Nombre, t2Apellido, RolUsuario.TUTOR, null);

        Usuario tutor1 = usuarioRepository.findByDni(t1Dni).orElse(null);
        Usuario tutor2 = usuarioRepository.findByDni(t2Dni).orElse(null);

        crearUsuarioSiNoExiste(e1Email, e1Dni, e1Nombre, e1Apellido, RolUsuario.ESTUDIANTE, tutor1);
        crearUsuarioSiNoExiste(e2Email, e2Dni, e2Nombre, e2Apellido, RolUsuario.ESTUDIANTE, tutor2);
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
                                         String apellido, RolUsuario rol, Usuario tutor) {
        if (usuarioRepository.findByEmail(email).isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .email(email)
                    .dni(dni)
                    .password(passwordEncoder.encode(dni))
                    .rol(rol)
                    .tutor(tutor)
                    .activo(true)
                    .primerIngreso(true)
                    .build());
        }
    }
}
