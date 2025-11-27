package com.example.gymweb.Controller;

import com.example.gymweb.Service.MercadoPagoService;
import com.example.gymweb.Service.PagoService;
import com.example.gymweb.dto.Request.PagoRequest;
import com.example.gymweb.dto.Response.MembresiaResponse;
import com.example.gymweb.dto.Response.MercadoPagoPreferenceResponse;
import com.example.gymweb.dto.Response.PagoResponse;
import com.example.gymweb.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/pagos"})
public class PagoController {
    private PagoService pagoService;
    private MercadoPagoService mercadoPagoService;

    @Autowired
    public PagoController(PagoService pagoService, MercadoPagoService mercadoPagoService) {
        this.pagoService = pagoService;
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@RequestBody PagoRequest request) {
        return ResponseEntity.ok(this.pagoService.registrarPago(request));
    }

    @GetMapping({"/membresia/{idMembresia}"})
    public ResponseEntity<List<PagoResponse>> pagosPorMembresia(@PathVariable int idMembresia) {
        return ResponseEntity.ok(this.pagoService.listarPagosPorMembresia(idMembresia));
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public ResponseEntity<List<PagoResponse>> pagosDeUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.pagoService.listarPagosDeUsuario(idUsuario));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        return ResponseEntity.ok(this.pagoService.listarTodos());
    }

    @PostMapping({"/mercadopago/preferencia"})
    public ResponseEntity<MercadoPagoPreferenceResponse> crearPreferencia(@RequestParam String plan) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario)) {
            return ResponseEntity.status(401).build();
        }
        Usuario usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(this.mercadoPagoService.crearPreferencia(plan, usuario));
    }

    @PostMapping({"/mercadopago/confirmar"})
    public ResponseEntity<MembresiaResponse> confirmarPago(@RequestParam Long paymentId) {
        return ResponseEntity.ok(this.pagoService.procesarPagoMercadoPago(paymentId));
    }
}
