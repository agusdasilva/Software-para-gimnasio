package com.example.gymweb.Controller;

import com.example.gymweb.Service.MercadoPagoService;
import com.example.gymweb.dto.Request.MercadoPagoPreferenceRequest;
import com.example.gymweb.dto.Response.MercadoPagoPreferenceResponse;
import com.example.gymweb.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/mercadopago"})
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping({"/preferencias"})
    public ResponseEntity<MercadoPagoPreferenceResponse> crearPreferencia(
            @RequestBody MercadoPagoPreferenceRequest request,
            @AuthenticationPrincipal Usuario usuario
    ) {
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(this.mercadoPagoService.crearPreferencia(request.getPlanCode(), usuario));
    }
}
