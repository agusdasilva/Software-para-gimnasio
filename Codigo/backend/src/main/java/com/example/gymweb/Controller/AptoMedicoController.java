package com.example.gymweb.Controller;

import com.example.gymweb.Service.AptoMedicoService;
import com.example.gymweb.dto.Request.AptoAprobarRequest;
import com.example.gymweb.dto.Request.AptoUploadRequest;
import com.example.gymweb.dto.Response.AptoResponse;
import com.example.gymweb.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aptos")
public class AptoMedicoController {

    private final AptoMedicoService service;

    public AptoMedicoController(AptoMedicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AptoResponse> subir(@RequestBody AptoUploadRequest request) {
        Usuario usuario = currentUser();
        return ResponseEntity.ok(service.subir(usuario, request));
    }

    @GetMapping("/mios")
    public ResponseEntity<List<AptoResponse>> mios() {
        Usuario usuario = currentUser();
        return ResponseEntity.ok(service.listarMios(usuario.getId()));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<AptoResponse>> pendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @GetMapping("/aprobados")
    public ResponseEntity<List<AptoResponse>> aprobados() {
        return ResponseEntity.ok(service.listarAprobadosVigentes());
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<AptoResponse> aprobar(@PathVariable Integer id, @RequestBody AptoAprobarRequest request) {
        return ResponseEntity.ok(service.aprobar(id, request));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<AptoResponse> rechazar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.rechazar(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<AptoResponse> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    private Usuario currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario)) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        return (Usuario) auth.getPrincipal();
    }
}
