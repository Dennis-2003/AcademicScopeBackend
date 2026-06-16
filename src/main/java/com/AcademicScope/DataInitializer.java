package com.AcademicScope;

import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.enums.TipoCurso;
import com.AcademicScope.model.Curso;
import com.AcademicScope.model.Grado;
import com.AcademicScope.model.Institucion;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.enums.EstadoMatricula;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.model.*;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.repository.academico.GradoRepository;
import com.AcademicScope.repository.institucion.InstitucionRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import com.AcademicScope.repository.matricula.MatriculaRepository;
import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.repository.horario.HorarioRepository;
import com.AcademicScope.repository.evaluacion.EvaluacionRepository;
import com.AcademicScope.repository.evaluacion.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final GradoRepository gradoRepository;
    private final CursoRepository cursoRepository;
    private final InstitucionRepository institucionRepository;
    private final MatriculaRepository matriculaRepository;
    private final HorarioRepository horarioRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final CalificacionRepository calificacionRepository;
    private final com.AcademicScope.repository.asignacion.AsignacionRepository asignacionRepository;
    private final com.AcademicScope.repository.recurso.RecursoRepository recursoRepository;
    private final com.AcademicScope.repository.comunicado.ComunicadoRepository comunicadoRepository;
    private final com.AcademicScope.repository.notificacion.NotificacionRepository notificacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    public void run(String... args) {
        crearInstitucionBase();
        crearUsuarios();
        crearGradosYCursos();
        crearDatosMockEstudiantes();
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

    private void crearDatosMockEstudiantes() {
        Usuario sofia = usuarioRepository.findByDni(env.getProperty("ESTUDIANTE4_DNI", "77777777")).orElse(null);
        Usuario pedro = usuarioRepository.findByDni(env.getProperty("ESTUDIANTE1_DNI", "11111111")).orElse(null);
        Usuario lucia = usuarioRepository.findByDni(env.getProperty("ESTUDIANTE2_DNI", "22222222")).orElse(null);
        Usuario marcos = usuarioRepository.findByDni(env.getProperty("ESTUDIANTE3_DNI", "66666666")).orElse(null);

        // Obtener a Mario y a un Admin
        Usuario mario = usuarioRepository.findByDni(env.getProperty("DOCENTE1_DNI", "12345678")).orElse(null);
        Usuario admin = usuarioRepository.findByEmail(env.getProperty("ADMIN1_EMAIL", "dennis@academicscope.com")).orElse(null);

        // Crear comunicado global si no existe
        if (comunicadoRepository.count() == 0 && admin != null) {
            comunicadoRepository.save(Comunicado.builder()
                    .titulo("Reunión de Coordinación Docente")
                    .contenido("Se convoca a todos los docentes a la reunión general de inicio de mes en el auditorio principal.")
                    .audiencia("DOCENTES")
                    .prioridad("ALTA")
                    .fecha(LocalDate.now())
                    .build());
            
            // Comunicado para estudiantes
            comunicadoRepository.save(Comunicado.builder()
                    .titulo("Inicio de Talleres Extracurriculares")
                    .contenido("Les recordamos a todos los estudiantes que este fin de semana inician los talleres.")
                    .audiencia("ESTUDIANTES")
                    .prioridad("NORMAL")
                    .fecha(LocalDate.now())
                    .build());
        }

        // Crear una notificación directa a Mario
        if (mario != null && notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(mario.getId()).isEmpty() && admin != null) {
            notificacionRepository.save(Notificacion.builder()
                    .usuario(mario)
                    .remitente(admin)
                    .titulo("Actualización de Sílabo Requerida")
                    .mensaje("Estimado Mario, por favor envíenos el sílabo actualizado de Matemáticas antes del viernes.")
                    .fechaEnvio(java.time.LocalDateTime.now())
                    .leido(false)
                    .build());
        }

        // 1. Configurar cursos
        Curso cursoMatematicas = cursoRepository.findByTipo(TipoCurso.REGULAR).stream()
            .filter(c -> c.getNombre().equals("Matemáticas"))
            .findFirst().orElse(null);
        
        Curso cursoMusica = cursoRepository.findByTipo(TipoCurso.TALLER).stream()
            .filter(c -> c.getNombre().equals("Música"))
            .findFirst().orElse(null);

        if (cursoMatematicas != null && cursoMusica != null) {
            // Asignarle a Mario la clase de Matemáticas
            if (mario != null) {
                cursoMatematicas.setDocente(mario);
                cursoRepository.save(cursoMatematicas);
            }

            // Crear Asignación y Recurso mock para Matemáticas
            if (asignacionRepository.findByCursoId(cursoMatematicas.getId()).isEmpty()) {
                asignacionRepository.save(Asignacion.builder()
                        .curso(cursoMatematicas)
                        .titulo("Ejercicios de Álgebra Avanzada")
                        .descripcion("Resolver la separata de ecuaciones de primer grado.")
                        .fechaRegistro(java.time.LocalDateTime.now().minusDays(1))
                        .fechaVencimiento(java.time.LocalDateTime.now().plusDays(3))
                        .estado("ACTIVA")
                        .build());
            }

            if (recursoRepository.findByCursoId(cursoMatematicas.getId()).isEmpty()) {
                recursoRepository.save(Recurso.builder()
                        .curso(cursoMatematicas)
                        .titulo("Guía de Matemáticas - Primer Bimestre")
                        .tipo("PDF")
                        .url("https://ejemplo.com/guia_mate.pdf")
                        .fechaSubida(java.time.LocalDateTime.now())
                        .build());
            }
            
            // Horarios (Si no existen)
            if (horarioRepository.findByCursoId(cursoMatematicas.getId()).isEmpty()) {
                horarioRepository.save(Horario.builder()
                        .curso(cursoMatematicas).diaSemana("LUNES")
                        .horaInicio("08:00").horaFin("10:00").aula("Pabellón A - 101").build());
                horarioRepository.save(Horario.builder()
                        .curso(cursoMatematicas).diaSemana("MIÉRCOLES")
                        .horaInicio("08:00").horaFin("10:00").aula("Pabellón A - 101").build());
                horarioRepository.save(Horario.builder()
                        .curso(cursoMatematicas).diaSemana("VIERNES")
                        .horaInicio("10:30").horaFin("12:00").aula("Pabellón A - 101").build());
            }
            if (horarioRepository.findByCursoId(cursoMusica.getId()).isEmpty()) {
                horarioRepository.save(Horario.builder()
                        .curso(cursoMusica).diaSemana("VIERNES")
                        .horaInicio("15:00").horaFin("17:00").aula("Sala de Música").build());
            }
            
            // Crear Evaluaciones (Si no existen)
            List<Evaluacion> evaluaciones = evaluacionRepository.findByCursoId(cursoMatematicas.getId());
            if (evaluaciones.isEmpty()) {
                evaluacionRepository.save(Evaluacion.builder()
                        .curso(cursoMatematicas).nombre("Examen Parcial").ponderacion(50.0).orden(1)
                        .fecha(LocalDate.now().minusDays(5)).build());
                evaluacionRepository.save(Evaluacion.builder()
                        .curso(cursoMatematicas).nombre("Práctica Calificada").ponderacion(50.0).orden(2)
                        .fecha(LocalDate.now().plusDays(3)).build()); 
                evaluacionRepository.save(Evaluacion.builder()
                        .curso(cursoMatematicas).nombre("Examen Final").ponderacion(50.0).orden(3)
                        .fecha(LocalDate.now().plusDays(10)).build()); 
                evaluaciones = evaluacionRepository.findByCursoId(cursoMatematicas.getId());
            }

            // INYECTAR DATOS PARA SOFIA
            if (sofia != null && matriculaRepository.findByEstudianteId(sofia.getId()).isEmpty()) {
                matriculaRepository.save(Matricula.builder()
                        .estudiante(sofia).grado(cursoMatematicas.getGrado()).seccion("A").estado(EstadoMatricula.ACTIVA)
                        .fechaMatricula(LocalDate.now().minusMonths(2)).build());

                // Calificaciones Sofia
                if (!evaluaciones.isEmpty()) {
                    calificacionRepository.save(Calificacion.builder()
                            .evaluacion(evaluaciones.get(0)).estudiante(sofia).nota(18.0)
                            .comentarioDocente("Excelente progreso").fechaRegistro(LocalDate.now().minusDays(15)).build());
                }

                // Asistencias Sofia
                for (int i = 1; i <= 10; i++) {
                    TipoAsistencia tipo = (i == 3) ? TipoAsistencia.TARDANZA : (i == 7 ? TipoAsistencia.FALTA : TipoAsistencia.PRESENTE);
                    asistenciaRepository.save(Asistencia.builder()
                            .estudiante(sofia).curso(cursoMatematicas)
                            .fecha(LocalDate.now().minusDays(i)).tipo(tipo).build());
                }
            }

            // INYECTAR DATOS PARA PEDRO (ESTUDIANTE 1)
            if (pedro != null && matriculaRepository.findByEstudianteId(pedro.getId()).isEmpty()) {
                // Matricular a Pedro
                matriculaRepository.save(Matricula.builder()
                        .estudiante(pedro).grado(cursoMatematicas.getGrado()).seccion("A").estado(EstadoMatricula.ACTIVA)
                        .fechaMatricula(LocalDate.now().minusMonths(2)).build());

                // Calificaciones Pedro (Para que el semáforo muestre datos, ej. notas mixtas)
                if (!evaluaciones.isEmpty()) {
                    calificacionRepository.save(Calificacion.builder()
                            .evaluacion(evaluaciones.get(0)).estudiante(pedro).nota(14.0)
                            .comentarioDocente("Buen trabajo, pero puedes mejorar").fechaRegistro(LocalDate.now().minusDays(15)).build());
                    
                    if (evaluaciones.size() > 1) {
                        calificacionRepository.save(Calificacion.builder()
                                .evaluacion(evaluaciones.get(1)).estudiante(pedro).nota(17.0)
                                .comentarioDocente("Excelente mejora en álgebra").fechaRegistro(LocalDate.now().minusDays(2)).build());
                    }
                }

                // Asistencias Pedro (Para que el panel muestre % de asistencia)
                for (int i = 1; i <= 10; i++) {
                    TipoAsistencia tipo = (i == 5) ? TipoAsistencia.FALTA : TipoAsistencia.PRESENTE;
                    asistenciaRepository.save(Asistencia.builder()
                            .estudiante(pedro).curso(cursoMatematicas)
                            .fecha(LocalDate.now().minusDays(i)).tipo(tipo).build());
                }
            }

            // INYECTAR DATOS PARA LUCIA (ESTUDIANTE 2)
            if (lucia != null && matriculaRepository.findByEstudianteId(lucia.getId()).isEmpty()) {
                matriculaRepository.save(Matricula.builder()
                        .estudiante(lucia).grado(cursoMatematicas.getGrado()).seccion("B").estado(EstadoMatricula.ACTIVA)
                        .fechaMatricula(LocalDate.now().minusMonths(2)).build());
                if (!evaluaciones.isEmpty()) {
                    calificacionRepository.save(Calificacion.builder()
                            .evaluacion(evaluaciones.get(0)).estudiante(lucia).nota(19.0)
                            .comentarioDocente("Excelente!").fechaRegistro(LocalDate.now().minusDays(15)).build());
                }
                for (int i = 1; i <= 10; i++) {
                    TipoAsistencia tipo = TipoAsistencia.PRESENTE;
                    asistenciaRepository.save(Asistencia.builder()
                            .estudiante(lucia).curso(cursoMatematicas)
                            .fecha(LocalDate.now().minusDays(i)).tipo(tipo).build());
                }
            }

            // INYECTAR DATOS PARA MARCOS (ESTUDIANTE 3)
            if (marcos != null && matriculaRepository.findByEstudianteId(marcos.getId()).isEmpty()) {
                matriculaRepository.save(Matricula.builder()
                        .estudiante(marcos).grado(cursoMatematicas.getGrado()).seccion("B").estado(EstadoMatricula.ACTIVA)
                        .fechaMatricula(LocalDate.now().minusMonths(2)).build());
                if (!evaluaciones.isEmpty()) {
                    calificacionRepository.save(Calificacion.builder()
                            .evaluacion(evaluaciones.get(0)).estudiante(marcos).nota(11.0)
                            .comentarioDocente("Debes esforzarte más.").fechaRegistro(LocalDate.now().minusDays(15)).build());
                }
                for (int i = 1; i <= 10; i++) {
                    TipoAsistencia tipo = (i % 2 == 0) ? TipoAsistencia.FALTA : TipoAsistencia.PRESENTE;
                    asistenciaRepository.save(Asistencia.builder()
                            .estudiante(marcos).curso(cursoMatematicas)
                            .fecha(LocalDate.now().minusDays(i)).tipo(tipo).build());
                }
            }
            
            System.out.println(" Datos Mock inyectados exitosamente para estudiantes y docentes");
        }
    }
}
