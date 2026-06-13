package com.AcademicScope.repository.notificacion;

import com.AcademicScope.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaEnvioDesc(Long usuarioId);
    List<Notificacion> findByUsuarioIdAndLeidoFalse(Long usuarioId);
    List<Notificacion> findByRemitenteIdOrderByFechaEnvioDesc(Long remitenteId);
}
