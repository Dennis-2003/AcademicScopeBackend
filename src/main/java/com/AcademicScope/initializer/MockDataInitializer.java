package com.AcademicScope.initializer;

import com.AcademicScope.enums.EstadoMatricula;
import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.enums.TipoConducta;
import com.AcademicScope.model.*;
import com.AcademicScope.repository.academico.CursoRepository;
import com.AcademicScope.repository.academico.GradoRepository;
import com.AcademicScope.repository.asistencia.AsistenciaRepository;
import com.AcademicScope.repository.comportamiento.ComportamientoRepository;
import com.AcademicScope.repository.comunicado.ComunicadoRepository;
import com.AcademicScope.repository.evaluacion.CalificacionRepository;
import com.AcademicScope.repository.evaluacion.EvaluacionRepository;
import com.AcademicScope.repository.finanzas.ConceptoPagoRepository;
import com.AcademicScope.repository.finanzas.PagoEstudianteRepository;
import com.AcademicScope.repository.asignacion.EntregaAsignacionRepository;
import com.AcademicScope.repository.horario.HorarioRepository;
import com.AcademicScope.repository.matricula.MatriculaRepository;
import com.AcademicScope.repository.recurso.RecursoRepository;
import com.AcademicScope.repository.asignacion.AsignacionRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.security.SecureRandom;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class MockDataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final GradoRepository gradoRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final CalificacionRepository calificacionRepository;
    private final ComportamientoRepository comportamientoRepository;
    private final ComunicadoRepository comunicadoRepository;
    private final HorarioRepository horarioRepository;
    private final RecursoRepository recursoRepository;
    private final AsignacionRepository asignacionRepository;
    private final EntregaAsignacionRepository entregaAsignacionRepository;
    private final ConceptoPagoRepository conceptoPagoRepository;
    private final PagoEstudianteRepository pagoEstudianteRepository;

    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    public void run(String... args) {
        log.info("--- Iniciando inyección completa de Datos Mock (Usuarios, Relaciones, Notas, Semáforos, etc.) ---");
        crearDocentesYTutoresIniciales();
        inyectarDatosMasivos();
        relacionarDatosBase();
        inyectarHorarios();
        inyectarAsistenciasNotasYComportamientos();
        inyectarRecursosYAsignaciones();
        inyectarComunicados();
        inyectarFinanzas();
        log.info("--- Inyección de Datos Mock totalmente completada ---");
    }

    private void crearDocentesYTutoresIniciales() {
        // Docentes
        crearUsuarioSiNoExiste(env.getProperty("DOCENTE1_EMAIL", "profesor.mario@academicscope.com"), env.getProperty("DOCENTE1_DNI", "12345678"), "Mario", "Gutierrez", env.getProperty("DOCENTE1_PASSWORD", "Profe@2026!"), RolUsuario.DOCENTE, null);
        crearUsuarioSiNoExiste(env.getProperty("DOCENTE2_EMAIL", "profesora.laura@academicscope.com"), env.getProperty("DOCENTE2_DNI", "87654321"), "Laura", "Condori", env.getProperty("DOCENTE2_PASSWORD", "Profe@2026!"), RolUsuario.DOCENTE, null);
        crearUsuarioSiNoExiste(env.getProperty("DOCENTE3_EMAIL", "profesor.carlos@academicscope.com"), env.getProperty("DOCENTE3_DNI", "11223344"), "Carlos", "Perez", env.getProperty("DOCENTE3_PASSWORD", "Profe@2026!"), RolUsuario.DOCENTE, null);

        // Tutores
        crearUsuarioSiNoExiste(env.getProperty("TUTOR1_EMAIL", "tutor.juan@academicscope.com"), env.getProperty("TUTOR1_DNI", "33333333"), "Juan", "Quispe", env.getProperty("TUTOR1_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);
        crearUsuarioSiNoExiste(env.getProperty("TUTOR2_EMAIL", "tutora.ana@academicscope.com"), env.getProperty("TUTOR2_DNI", "44444444"), "Ana", "Mamani", env.getProperty("TUTOR2_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);
        crearUsuarioSiNoExiste(env.getProperty("TUTOR3_EMAIL", "tutor.luis@academicscope.com"), env.getProperty("TUTOR3_DNI", "55555555"), "Luis", "Sanchez", env.getProperty("TUTOR3_PASSWORD", "Tutor@2026!"), RolUsuario.TUTOR, null);

        Usuario tutor1 = usuarioRepository.findByDni(env.getProperty("TUTOR1_DNI", "33333333")).orElse(null);
        Usuario tutor2 = usuarioRepository.findByDni(env.getProperty("TUTOR2_DNI", "44444444")).orElse(null);

        // Estudiantes iniciales
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE1_EMAIL", "estudiante.pedro@academicscope.com"), env.getProperty("ESTUDIANTE1_DNI", "11111111"), "Pedro", "Quispe", env.getProperty("ESTUDIANTE1_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor1);
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE2_EMAIL", "estudiante.lucia@academicscope.com"), env.getProperty("ESTUDIANTE2_DNI", "22222222"), "Lucia", "Quispe", env.getProperty("ESTUDIANTE2_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor1);
        crearUsuarioSiNoExiste(env.getProperty("ESTUDIANTE3_EMAIL", "estudiante.marcos@academicscope.com"), env.getProperty("ESTUDIANTE3_DNI", "66666666"), "Marcos", "Mamani", env.getProperty("ESTUDIANTE3_PASSWORD", "Estu@2026!"), RolUsuario.ESTUDIANTE, tutor2);
    }

    private void inyectarDatosMasivos() {
        List<Usuario> tutores = usuarioRepository.findByRol(RolUsuario.TUTOR);
        SecureRandom random = new SecureRandom();

        for (int i = 1; i <= 20; i++) {
            Usuario tutorAleatorio = tutores.isEmpty() ? null : tutores.get(random.nextInt(tutores.size()));
            crearUsuarioSiNoExiste("estudiante.mock" + i + "@academicscope.com", "888800" + String.format("%02d", i), "EstudianteMock" + i, "Prueba", "Estu@2026!", RolUsuario.ESTUDIANTE, tutorAleatorio);
        }
        for (int i = 1; i <= 5; i++) {
            crearUsuarioSiNoExiste("docente.mock" + i + "@academicscope.com", "777700" + String.format("%02d", i), "DocenteMock" + i, "Prueba", "Profe@2026!", RolUsuario.DOCENTE, null);
        }
    }

    private void relacionarDatosBase() {
        List<Usuario> estudiantes = usuarioRepository.findByRol(RolUsuario.ESTUDIANTE);
        List<Usuario> docentes = usuarioRepository.findByRol(RolUsuario.DOCENTE);
        List<Grado> grados = gradoRepository.findAll();
        List<Curso> cursos = cursoRepository.findAll();
        SecureRandom random = new SecureRandom();

        if (grados.isEmpty() || cursos.isEmpty()) return;

        // Matriculas
        for (Usuario est : estudiantes) {
            if (matriculaRepository.findByEstudianteId(est.getId()).isEmpty()) {
                matriculaRepository.save(Matricula.builder()
                        .estudiante(est)
                        .grado(grados.get(random.nextInt(grados.size())))
                        .seccion(random.nextBoolean() ? "A" : "B")
                        .estado(EstadoMatricula.ACTIVA)
                        .fechaMatricula(LocalDate.now().minusDays(random.nextInt(30)))
                        .build());
            }
        }

        // Asignar cursos a docentes
        for (Curso curso : cursos) {
            if (curso.getDocente() == null && !docentes.isEmpty()) {
                curso.setDocente(docentes.get(random.nextInt(docentes.size())));
                cursoRepository.save(curso);
            }
        }
    }

    private void inyectarAsistenciasNotasYComportamientos() {
        List<Matricula> matriculas = matriculaRepository.findAll();
        List<Curso> cursos = cursoRepository.findAll();
        SecureRandom random = new SecureRandom();

        if (matriculas.isEmpty() || cursos.isEmpty()) return;

        if (evaluacionRepository.count() == 0) {
            log.info("Inyectando evaluaciones, calificaciones, asistencias y semáforos (comportamiento)...");
            
            for (Matricula matricula : matriculas) {
                Usuario estudiante = matricula.getEstudiante();
                Grado grado = matricula.getGrado();
                
                // Buscar cursos de este grado
                List<Curso> cursosDelGrado = cursoRepository.findByGradoId(grado.getId());

                for (Curso curso : cursosDelGrado) {
                    Usuario docente = curso.getDocente();
                    if (docente == null) continue;

                    // 1. Asistencia (Últimos 5 días)
                    for (int d = 1; d <= 5; d++) {
                        TipoAsistencia tipo = TipoAsistencia.PRESENTE;
                        if (random.nextInt(10) > 8) tipo = TipoAsistencia.FALTA; // 10% faltas
                        else if (random.nextInt(10) > 7) tipo = TipoAsistencia.TARDANZA; // 10% tardanza

                        asistenciaRepository.save(Asistencia.builder()
                                .estudiante(estudiante)
                                .curso(curso)
                                .fecha(LocalDate.now().minusDays(d))
                                .tipo(tipo)
                                .observacion(tipo == TipoAsistencia.FALTA ? "Falta injustificada" : "")
                                .build());
                    }

                    // 2. Evaluaciones y Calificaciones (Notas)
                    // Creamos 1 evaluacion por curso (o la buscamos)
                    Evaluacion eval = evaluacionRepository.findAll().stream()
                            .filter(e -> e.getCurso().getId().equals(curso.getId()))
                            .findFirst()
                            .orElseGet(() -> evaluacionRepository.save(Evaluacion.builder()
                                    .curso(curso)
                                    .nombre("Examen Parcial - " + curso.getNombre())
                                    .descripcion("Evaluación de la unidad 1")
                                    .fecha(LocalDate.now().minusDays(random.nextInt(10)))
                                    .ponderacion(1.0)
                                    .orden(1)
                                    .build()));

                    calificacionRepository.save(Calificacion.builder()
                            .evaluacion(eval)
                            .estudiante(estudiante)
                            .nota(10.0 + random.nextInt(11)) // Nota entre 10 y 20
                            .comentarioDocente("Buen esfuerzo.")
                            .fechaRegistro(LocalDate.now())
                            .build());

                    // 3. Comportamiento (Semáforo)
                    TipoConducta tipoConducta = TipoConducta.VERDE;
                    int r = random.nextInt(10);
                    if (r > 8) tipoConducta = TipoConducta.ROJO;
                    else if (r > 6) tipoConducta = TipoConducta.AMARILLO;

                    comportamientoRepository.save(Comportamiento.builder()
                            .estudiante(estudiante)
                            .docente(docente)
                            .tipo(tipoConducta)
                            .descripcion("Observación de conducta durante la clase de " + curso.getNombre())
                            .fecha(LocalDate.now().minusDays(random.nextInt(5)))
                            .puntaje(tipoConducta == TipoConducta.VERDE ? 20 : tipoConducta == TipoConducta.AMARILLO ? 15 : 10)
                            .calificacionLiteral("A")
                            .build());
                }
            }
        }
    }

    private void inyectarRecursosYAsignaciones() {
        if (recursoRepository.count() == 0 && asignacionRepository.count() == 0) {
            log.info("Inyectando recursos (materiales) y asignaciones (tareas)...");
            List<Curso> cursos = cursoRepository.findAll();
            
            for (Curso curso : cursos) {
                // Agregar un material de estudio PDF
                recursoRepository.save(Recurso.builder()
                        .curso(curso)
                        .titulo("Sílabo del curso - " + curso.getNombre())
                        .tipo("PDF")
                        .url("https://example.com/silabo.pdf")
                        .tamano("1.2 MB")
                        .fechaSubida(java.time.LocalDateTime.now().minusDays(10))
                        .build());
                
                // Agregar una tarea pendiente
                Asignacion nuevaAsignacion = asignacionRepository.save(Asignacion.builder()
                        .curso(curso)
                        .titulo("Tarea 1: Investigacin sobre " + curso.getNombre())
                        .descripcion("Por favor subir el archivo PDF con su investigacin.")
                        .estado("ACTIVA")
                        .fechaRegistro(java.time.LocalDateTime.now().minusDays(2))
                        .fechaVencimiento(java.time.LocalDateTime.now().plusDays(5))
                        .build());

                // Agregar entregas de alumnos
                List<Usuario> estudiantes = usuarioRepository.findByRol(RolUsuario.ESTUDIANTE);
                SecureRandom random = new SecureRandom();
                for (Usuario est : estudiantes) {
                    if (random.nextBoolean()) {
                        entregaAsignacionRepository.save(EntregaAsignacion.builder()
                                .asignacion(nuevaAsignacion)
                                .estudiante(est)
                                .fechaEntrega(java.time.LocalDateTime.now().minusHours(random.nextInt(24)))
                                .estado("ENTREGADO")
                                .archivoUrl("https://example.com/tarea_resumen_" + est.getNombre() + ".pdf")
                                .cumplio(true)
                                .retroalimentacion("Buen trabajo")
                                .build());
                    }
                }
            }
        }
    }

    private void inyectarComunicados() {
        if (comunicadoRepository.count() == 0) {
            log.info("Inyectando comunicados y notificaciones generales...");
            comunicadoRepository.save(Comunicado.builder()
                    .titulo("Bienvenida al Año Escolar")
                    .contenido("Estimada comunidad educativa, les damos la bienvenida al presente ciclo académico. Recuerden revisar sus horarios.")
                    .audiencia("TODOS")
                    .prioridad("ALTA")
                    .fecha(LocalDate.now())
                    .build());

            comunicadoRepository.save(Comunicado.builder()
                    .titulo("Reunión de Apoderados")
                    .contenido("Se convoca a los tutores y apoderados a la primera reunión del semestre.")
                    .audiencia("APODERADOS")
                    .prioridad("MEDIA")
                    .fecha(LocalDate.now().minusDays(2))
                    .build());
        }
    }

    private void inyectarHorarios() {
        if (horarioRepository.count() < 10) { // Si hay muy pocos horarios (como los recreos manuales)
            log.info("Inyectando horarios para los cursos...");
            List<Curso> cursos = cursoRepository.findAll();
            String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};
            String[] aulas = {"Aula 101", "Aula 102", "Aula 201", "Lab Ciencias", "Lab Cómputo"};
            SecureRandom random = new SecureRandom();

            for (Curso curso : cursos) {
                // Asignar al curso 2 bloques de clases en días diferentes
                int d1 = random.nextInt(5);
                int d2 = (d1 + 1 + random.nextInt(4)) % 5; // Asegurar día distinto
                
                String aulaAleatoria = aulas[random.nextInt(aulas.length)];

                // Clase 1
                horarioRepository.save(Horario.builder()
                        .curso(curso)
                        .diaSemana(dias[d1])
                        .horaInicio("08:00")
                        .horaFin("10:00")
                        .aula(aulaAleatoria)
                        .build());

                // Clase 2
                horarioRepository.save(Horario.builder()
                        .curso(curso)
                        .diaSemana(dias[d2])
                        .horaInicio("11:00")
                        .horaFin("13:00")
                        .aula(aulaAleatoria)
                        .build());
            }
        }
    }

    private void inyectarFinanzas() {
        if (conceptoPagoRepository.count() == 0) {
            log.info("Inyectando conceptos de pago y pagos de estudiantes...");
            
            ConceptoPago matricula = conceptoPagoRepository.save(ConceptoPago.builder()
                    .nombre("Matrcula 2026")
                    .monto(150.0)
                    .build());

            ConceptoPago pensionMarzo = conceptoPagoRepository.save(ConceptoPago.builder()
                    .nombre("Pensin Marzo 2026")
                    .monto(300.0)
                    .build());
            
            ConceptoPago pensionAbril = conceptoPagoRepository.save(ConceptoPago.builder()
                    .nombre("Pensin Abril 2026")
                    .monto(300.0)
                    .build());

            List<Usuario> estudiantes = usuarioRepository.findByRol(RolUsuario.ESTUDIANTE);
            SecureRandom random = new SecureRandom();

            for (Usuario estudiante : estudiantes) {
                // Pago de matricula (todos pagados)
                pagoEstudianteRepository.save(PagoEstudiante.builder()
                        .estudiante(estudiante)
                        .concepto(matricula)
                        .pagado(true)
                        .fechaPago(java.time.LocalDateTime.now().minusMonths(3))
                        .build());

                // Pago de Marzo (casi todos pagados)
                boolean pagoMarzo = random.nextInt(10) > 1; // 90% pag
                pagoEstudianteRepository.save(PagoEstudiante.builder()
                        .estudiante(estudiante)
                        .concepto(pensionMarzo)
                        .pagado(pagoMarzo)
                        .fechaPago(pagoMarzo ? java.time.LocalDateTime.now().minusMonths(2) : null)
                        .build());

                // Pago de Abril (algunos deben)
                boolean pagoAbril = random.nextInt(10) > 4; // 60% pag
                pagoEstudianteRepository.save(PagoEstudiante.builder()
                        .estudiante(estudiante)
                        .concepto(pensionAbril)
                        .pagado(pagoAbril)
                        .fechaPago(pagoAbril ? java.time.LocalDateTime.now().minusMonths(1) : null)
                        .build());
            }
        }
    }

    private void crearUsuarioSiNoExiste(String email, String dni, String nombre, String apellido, String rawPassword, RolUsuario rol, Usuario tutor) {
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
        }
    }
}
