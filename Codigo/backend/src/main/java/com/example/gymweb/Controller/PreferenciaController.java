package com.example.gymweb.controller;

import com.example.gymweb.Service.PreferenciaService;
import com.example.gymweb.dto.Request.PreferenciaRequest;
import com.example.gymweb.dto.Response.PreferenciaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/preferencia"})
public class PreferenciaController {
    private PreferenciaService service;

    @Autowired
    public PreferenciaController(PreferenciaService service) {
        this.service = service;
    }

    @GetMapping({"/{idUsuario}"})
    public ResponseEntity<PreferenciaResponse> obtener(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.service.obtener(idUsuario));
    }

    @PutMapping({"/{idUsuario}"})
    public ResponseEntity<PreferenciaResponse> actualizar(@PathVariable int idUsuario, @RequestBody PreferenciaRequest request) {
        return ResponseEntity.ok(this.service.actualizar(idUsuario, request.isSilencioso()));
    }
}
