package com.example.gymweb.controller;

import com.example.gymweb.dto.Request.UsuarioChangeEstadoRequest;
import com.example.gymweb.dto.Request.UsuarioChangeRolRequest;
import com.example.gymweb.dto.Request.UsuarioPerfilUpdateRequest;
import com.example.gymweb.dto.Response.UsuarioResponse;
import com.example.gymweb.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @PathVariable Integer id,
            @RequestBody UsuarioPerfilUpdateRequest request
    ) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(id, request));
    }

    @PatchMapping("/rol")
    public ResponseEntity<UsuarioResponse> cambiarRol(
            @RequestBody UsuarioChangeRolRequest request
    ) {
        return ResponseEntity.ok(usuarioService.cambiarRol(request));
    }

    @PatchMapping("/estado")
    public ResponseEntity<UsuarioResponse> cambiarEstado(
            @RequestBody UsuarioChangeEstadoRequest request
    ) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(request));
    }
}
