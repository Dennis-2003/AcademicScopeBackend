package com.AcademicScope.service.notificacion;

import com.AcademicScope.repository.notificacion.NotificacionRepository;
import com.AcademicScope.model.Notificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public Notificacion enviar(Notificacion notificacion) {
        notificacion.setLeido(false);
        notificacion.setFechaEnvio(LocalDateTime.now());
        return notificacionRepository.save(notificacion);
    }

    public Notificacion obtenerPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));
    }

    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId);
    }

    public List<Notificacion> listarNoLeidas(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeidoFalse(usuarioId);
    }

    public void marcarLeida(Long id) {
        notificacionRepository.findById(id).ifPresent(n -> {
            n.setLeido(true);
            notificacionRepository.save(n);
        });
    }

    public void eliminar(Long id) {
        notificacionRepository.deleteById(id);
    }
}
