package com.example.gymweb.Controller;

import com.example.gymweb.Service.NotificacionService;
import com.example.gymweb.dto.Request.NotificacionRequest;
import com.example.gymweb.dto.Response.NotificacionResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/notificaciones"})
public class NotificacionController {
    private NotificacionService service;

    @Autowired
    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @GetMapping({"/{idUsuario}"})
    public ResponseEntity<List<NotificacionResponse>> listar(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.service.listar(idUsuario));
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> crear(@RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(this.service.crear(request));
    }

    @PatchMapping({"/{id}/leer"})
    public ResponseEntity<String> leer(@PathVariable int id) {
        return ResponseEntity.ok(this.service.marcarLeida(id));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        this.service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}