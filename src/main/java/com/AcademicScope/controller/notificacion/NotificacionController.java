package com.AcademicScope.controller.notificacion;

import com.AcademicScope.service.notificacion.NotificacionService;
import com.AcademicScope.model.Notificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<Notificacion> enviar(@RequestBody Notificacion notificacion) {
        return new ResponseEntity<>(notificacionService.enviar(notificacion), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> listarNoLeidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarNoLeidas(usuarioId));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarLeida(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
