package com.AcademicScope.service.comunicado;

import com.AcademicScope.model.Comunicado;
import com.AcademicScope.repository.comunicado.ComunicadoRepository;
import com.AcademicScope.service.CloudinaryService;
import com.AcademicScope.service.CloudinaryServiceStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunicadoServiceTest {

    @Mock
    private ComunicadoRepository comunicadoRepository;
    private CloudinaryService cloudinaryService;
    private ComunicadoService comunicadoService;
    private Comunicado comunicado;

    @BeforeEach
    void setUp() {
        cloudinaryService = new CloudinaryServiceStub();
        comunicadoService = new ComunicadoService(comunicadoRepository, cloudinaryService);
        comunicado = Comunicado.builder()
                .id(1L)
                .titulo("Aviso importante")
                .contenido("Contenido del aviso")
                .audiencia("TODOS")
                .prioridad("ALTA")
                .build();
    }

    @Test
    void deberia_listar_comunicados_ordenados() {
        when(comunicadoRepository.findAllByOrderByFechaDescIdDesc()).thenReturn(List.of(comunicado));

        List<Comunicado> resultado = comunicadoService.listarComunicados();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_crear_comunicado_sin_archivo() {
        when(comunicadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Comunicado resultado = comunicadoService.crearComunicado(comunicado, null);

        assertNotNull(resultado.getFecha());
        assertEquals(LocalDate.now(), resultado.getFecha());
        assertNull(resultado.getArchivoUrl());
        verify(comunicadoRepository).save(comunicado);
    }

    @Test
    void deberia_crear_comunicado_con_archivo() throws Exception {
        MultipartFile archivo = mock(MultipartFile.class);
        when(archivo.isEmpty()).thenReturn(false);
        when(archivo.getOriginalFilename()).thenReturn("documento.pdf");

        when(comunicadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Comunicado resultado = comunicadoService.crearComunicado(comunicado, archivo);

        assertEquals("documento.pdf", resultado.getArchivoNombre());
        assertEquals("https://stub.cloudinary.com/auto/academicscope/comunicados/documento.pdf", resultado.getArchivoUrl());
        verify(comunicadoRepository).save(comunicado);
    }

    @Test
    void deberia_crear_comunicado_con_fecha_existente_sin_sobrescribir() {
        comunicado.setFecha(LocalDate.of(2025, 1, 15));
        when(comunicadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Comunicado resultado = comunicadoService.crearComunicado(comunicado, null);

        assertEquals(LocalDate.of(2025, 1, 15), resultado.getFecha());
    }

    @Test
    void deberia_eliminar_comunicado() {
        comunicadoService.eliminarComunicado(1L);
        verify(comunicadoRepository).deleteById(1L);
    }
}
