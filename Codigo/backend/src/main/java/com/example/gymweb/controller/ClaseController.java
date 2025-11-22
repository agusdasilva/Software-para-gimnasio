package com.example.gymweb.controller;

import com.example.gymweb.dto.Request.AprobacionRequest;
import com.example.gymweb.dto.Request.ClaseRequest;
import com.example.gymweb.dto.Request.InvitacionRequest;
import com.example.gymweb.dto.Response.ClaseResponse;
import com.example.gymweb.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @PostMapping
    public ResponseEntity<ClaseResponse> crearClase(@RequestBody ClaseRequest request) {
        return ResponseEntity.ok(claseService.crearClase(request));
    }

    @PostMapping("/{idClase}/invitar")
    public ResponseEntity<String> invitar(
            @PathVariable int idClase,
            @RequestBody InvitacionRequest request) {

        return ResponseEntity.ok(
                claseService.invitarUsuario(idClase, request.getIdUsuario())
        );
    }

    @PostMapping("/invitacion/{idInv}/responder")
    public ResponseEntity<String> responder(
            @PathVariable int idInv,
            @RequestBody AprobacionRequest request) {

        return ResponseEntity.ok(
                claseService.responderInvitacion(idInv, request.isAprobar())
        );
    }
}
