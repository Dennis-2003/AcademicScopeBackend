package com.AcademicScope.service.finanzas;

import com.AcademicScope.model.ConceptoPago;
import com.AcademicScope.model.PagoEstudiante;
import com.AcademicScope.model.Usuario;
import com.AcademicScope.repository.finanzas.ConceptoPagoRepository;
import com.AcademicScope.repository.finanzas.PagoEstudianteRepository;
import com.AcademicScope.repository.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanzasServiceTest {

    @Mock
    private ConceptoPagoRepository conceptoPagoRepository;
    @Mock
    private PagoEstudianteRepository pagoEstudianteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private FinanzasService finanzasService;

    private ConceptoPago concepto;
    private Usuario estudiante;
    private PagoEstudiante pago;

    @BeforeEach
    void setUp() {
        concepto = ConceptoPago.builder().id(1L).nombre("Matrícula").monto(100.0).build();
        estudiante = Usuario.builder().id(1L).nombre("Pedro").build();
        pago = PagoEstudiante.builder()
                .id(1L)
                .estudiante(estudiante)
                .concepto(concepto)
                .pagado(true)
                .build();
    }

    @Test
    void deberia_listar_conceptos() {
        when(conceptoPagoRepository.findAll()).thenReturn(List.of(concepto));

        List<ConceptoPago> resultado = finanzasService.listarConceptos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_crear_concepto() {
        when(conceptoPagoRepository.save(any())).thenReturn(concepto);

        ConceptoPago resultado = finanzasService.crearConcepto(concepto);

        assertEquals("Matrícula", resultado.getNombre());
    }

    @Test
    void deberia_listar_pagos() {
        when(pagoEstudianteRepository.findAll()).thenReturn(List.of(pago));

        List<PagoEstudiante> resultado = finanzasService.listarPagos();

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_pagos_por_estudiante() {
        when(pagoEstudianteRepository.findByEstudianteId(1L)).thenReturn(List.of(pago));

        List<PagoEstudiante> resultado = finanzasService.listarPagosPorEstudiante(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_toggle_pago_existente_a_no_pagado() {
        when(pagoEstudianteRepository.findByEstudianteIdAndConceptoId(1L, 1L))
                .thenReturn(Optional.of(pago));
        when(pagoEstudianteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PagoEstudiante resultado = finanzasService.togglePago(1L, 1L);

        assertFalse(resultado.getPagado());
        assertNull(resultado.getFechaPago());
    }

    @Test
    void deberia_toggle_pago_existente_a_pagado() {
        pago.setPagado(false);
        when(pagoEstudianteRepository.findByEstudianteIdAndConceptoId(1L, 1L))
                .thenReturn(Optional.of(pago));
        when(pagoEstudianteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PagoEstudiante resultado = finanzasService.togglePago(1L, 1L);

        assertTrue(resultado.getPagado());
        assertNotNull(resultado.getFechaPago());
    }

    @Test
    void deberia_crear_nuevo_pago_si_no_existe() {
        when(pagoEstudianteRepository.findByEstudianteIdAndConceptoId(1L, 1L))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(conceptoPagoRepository.findById(1L)).thenReturn(Optional.of(concepto));
        when(pagoEstudianteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PagoEstudiante resultado = finanzasService.togglePago(1L, 1L);

        assertTrue(resultado.getPagado());
        assertNotNull(resultado.getFechaPago());
        assertEquals(estudiante, resultado.getEstudiante());
        assertEquals(concepto, resultado.getConcepto());
    }

    @Test
    void deberia_lanzar_excepcion_si_estudiante_no_existe_en_toggle() {
        when(pagoEstudianteRepository.findByEstudianteIdAndConceptoId(99L, 1L))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> finanzasService.togglePago(99L, 1L));
    }

    @Test
    void deberia_lanzar_excepcion_si_concepto_no_existe_en_toggle() {
        when(pagoEstudianteRepository.findByEstudianteIdAndConceptoId(1L, 99L))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(conceptoPagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> finanzasService.togglePago(1L, 99L));
    }
}
