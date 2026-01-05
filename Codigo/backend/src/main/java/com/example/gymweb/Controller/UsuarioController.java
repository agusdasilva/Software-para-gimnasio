package com.example.gymweb.Controller;

import com.example.gymweb.Service.UsuarioService;
import com.example.gymweb.dto.Request.UsuarioChangeEstadoRequest;
import com.example.gymweb.dto.Request.UsuarioChangeRolRequest;
import com.example.gymweb.dto.Request.UsuarioPerfilUpdateRequest;
import com.example.gymweb.dto.Response.UsuarioResponse;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping({"/api/usuarios"})
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer id) {
        if (!isOwnerOrAdmin(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.usuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(this.usuarioService.listarTodos());
    }

    @PutMapping({"/{id}/perfil"})
    public ResponseEntity<UsuarioResponse> actualizarPerfil(@PathVariable Integer id, @RequestBody UsuarioPerfilUpdateRequest request) {
        if (!isOwnerOrAdmin(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(this.usuarioService.actualizarPerfil(id, request));
    }

    @PatchMapping({"/rol"})
    public ResponseEntity<UsuarioResponse> cambiarRol(@RequestBody UsuarioChangeRolRequest request) {
        return ResponseEntity.ok(this.usuarioService.cambiarRol(request));
    }

    @PatchMapping({"/estado"})
    public ResponseEntity<UsuarioResponse> cambiarEstado(@RequestBody UsuarioChangeEstadoRequest request) {
        return ResponseEntity.ok(this.usuarioService.cambiarEstado(request));
    }

    private boolean isOwnerOrAdmin(Integer targetUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) return true;
        }
        if (auth != null && auth.getPrincipal() instanceof com.example.gymweb.model.Usuario usuario) {
            return usuario.getId() == targetUserId;
        }
        return false;
    }
}
