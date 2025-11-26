package com.example.gymweb.Controller;


import com.example.gymweb.Service.ClaseService;
import com.example.gymweb.dto.Request.AprobacionRequest;
import com.example.gymweb.dto.Request.ClaseRequest;
import com.example.gymweb.dto.Request.InvitacionRequest;
import com.example.gymweb.dto.Request.MensajeClaseRequest;
import com.example.gymweb.dto.Response.ClaseResponse;
import com.example.gymweb.dto.Response.InvitacionResponse;
import com.example.gymweb.dto.Response.MensajeClaseResponse;
import com.example.gymweb.dto.Response.UsuarioResponse;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/clases"})
public class ClaseController {
    private ClaseService claseService;

    @Autowired
    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @PostMapping
    public ResponseEntity<ClaseResponse> crearClase(@RequestBody ClaseRequest request) {
        return ResponseEntity.ok(this.claseService.crearClase(request));
    }

    @GetMapping
    public ResponseEntity<List<ClaseResponse>> listar() {
        return ResponseEntity.ok(this.claseService.listar());
    }

    @GetMapping({"/{idClase}"})
    public ResponseEntity<ClaseResponse> buscarPorId(@PathVariable int idClase) {
        return ResponseEntity.ok(this.claseService.buscarPorId(idClase));
    }

    @GetMapping({"/{idClase}/miembros"})
    public ResponseEntity<List<UsuarioResponse>> miembros(@PathVariable int idClase) {
        return ResponseEntity.ok(this.claseService.listarMiembros(idClase));
    }

    @PostMapping({"/{idClase}/rutinas/{idRutina}"})
    public ResponseEntity<String> agregarRutina(@PathVariable int idClase, @PathVariable int idRutina) {
        return ResponseEntity.ok(this.claseService.agregarRutina(idClase, idRutina));
    }

    @GetMapping({"/{idClase}/invitaciones"})
    public ResponseEntity<List<InvitacionResponse>> invitaciones(@PathVariable int idClase) {
        return ResponseEntity.ok(this.claseService.listarInvitaciones(idClase));
    }

    @PostMapping({"/{idClase}/invitar"})
    public ResponseEntity<String> invitar(@PathVariable int idClase, @RequestBody InvitacionRequest request) {
        return ResponseEntity.ok(this.claseService.invitarUsuario(idClase, request.getIdUsuario()));
    }

    @PostMapping({"/invitacion/{idInv}/responder"})
    public ResponseEntity<String> responder(@PathVariable int idInv, @RequestBody AprobacionRequest request) {
        return ResponseEntity.ok(this.claseService.responderInvitacion(idInv, request.isAprobar()));
    }

    @PostMapping({"/{idClase}/mensajes"})
    public ResponseEntity<String> enviarMensaje(@PathVariable int idClase, @RequestBody MensajeClaseRequest request) {
        return ResponseEntity.ok(this.claseService.enviarMensaje(idClase, request));
    }

    @GetMapping({"/{idClase}/mensajes"})
    public ResponseEntity<List<MensajeClaseResponse>> mensajes(@PathVariable int idClase) {
        return ResponseEntity.ok(this.claseService.listarMensajes(idClase));
    }

    @DeleteMapping({"/{idClase}"})
    public ResponseEntity<Void> eliminar(@PathVariable int idClase) {
        this.claseService.eliminar(idClase);
        return ResponseEntity.noContent().build();
    }

    @PutMapping({"/{idClase}"})
    public ResponseEntity<ClaseResponse> editar(@PathVariable int idClase, @RequestBody ClaseRequest request) {
        return ResponseEntity.ok(this.claseService.editarClase(idClase, request));
    }
}
