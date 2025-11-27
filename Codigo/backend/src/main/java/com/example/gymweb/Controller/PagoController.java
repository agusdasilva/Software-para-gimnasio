package com.example.gymweb.Controller;

import com.example.gymweb.Service.PagoService;
import com.example.gymweb.dto.Request.PagoRequest;
import com.example.gymweb.dto.Response.PagoResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/pagos"})
public class PagoController {
    private PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
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
}
