package com.example.gymweb.Auth;
import com.example.gymweb.dto.Request.ActivarMembresiaRequest;
import com.example.gymweb.dto.Request.UsuarioLoginRequest;
import com.example.gymweb.dto.Request.UsuarioRegisterRequest;
import com.example.gymweb.dto.Response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gymweb.dto.Request.ActivarMembresiaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping({"/api/auth"})
    public class AuthController {
        private final AuthenticationService authService;

        public AuthController(AuthenticationService authService) {
            this.authService = authService;
        }

        @PostMapping({"/activar/{idUsuario}"})
        public ResponseEntity<String> activarMembresia(@PathVariable Integer idUsuario, @RequestBody ActivarMembresiaRequest request) {
            authService.activarCuentaPorMembresia(idUsuario, request.getCodigo());
            return ResponseEntity.ok("Cuenta activada correctamente");
        }
    }
