package com.AcademicScope.service.notificacion;

import com.AcademicScope.model.Notificacion;
import com.AcademicScope.repository.notificacion.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;
    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = Notificacion.builder()
                .id(1L)
                .titulo("Notificación de prueba")
                .mensaje("Mensaje de prueba")
                .leido(false)
                .fechaEnvio(LocalDateTime.now())
                .build();
    }

    @Test
    void deberia_enviar_notificacion_con_valores_iniciales() {
        when(notificacionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notificacion resultado = notificacionService.enviar(notificacion);

        assertFalse(resultado.getLeido());
        assertNotNull(resultado.getFechaEnvio());
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void deberia_obtener_notificacion_por_id() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        Notificacion resultado = notificacionService.obtenerPorId(1L);

        assertEquals("Notificación de prueba", resultado.getTitulo());
    }

    @Test
    void deberia_listar_por_usuario() {
        when(notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(1L))
                .thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listarPorUsuario(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_listar_no_leidas() {
        when(notificacionRepository.findByUsuarioIdAndLeidoFalse(1L))
                .thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listarNoLeidas(1L);

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getLeido());
    }

    @Test
    void deberia_listar_enviadas_por_usuario() {
        when(notificacionRepository.findByRemitenteIdOrderByFechaEnvioDesc(1L))
                .thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listarEnviadasPorUsuario(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberia_marcar_como_leida() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        notificacionService.marcarLeida(1L);

        assertTrue(notificacion.getLeido());
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void deberia_no_fallar_al_marcar_leida_inexistente() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        notificacionService.marcarLeida(99L);

        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void deberia_eliminar_notificacion() {
        notificacionService.eliminar(1L);
        verify(notificacionRepository).deleteById(1L);
    }
}
