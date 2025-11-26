package com.example.gymweb.controller;


import com.example.gymweb.Service.MembresiaService;
import com.example.gymweb.dto.Request.MembresiaRequest;
import com.example.gymweb.dto.Response.MembresiaResponse;
import com.example.gymweb.model.EstadoMembresia;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/membresias"})
public class MembresiaController {
    private MembresiaService membresiaService;

    @Autowired
    public MembresiaController(MembresiaService membresiaService) {
        this.membresiaService = membresiaService;
    }

    @PostMapping
    public ResponseEntity<MembresiaResponse> crear(@RequestBody MembresiaRequest request) {
        return ResponseEntity.ok(this.membresiaService.crearMembresia(request));
    }

    @GetMapping({"/usuario/{idUsuario}/actual"})
    public ResponseEntity<MembresiaResponse> obtenerActual(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.membresiaService.obtenerMembresiaActualPorUsuario(idUsuario));
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public ResponseEntity<List<MembresiaResponse>> listarPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.membresiaService.listarMembresiasPorUsuario(idUsuario));
    }

    @GetMapping
    public ResponseEntity<List<MembresiaResponse>> listarTodas() {
        return ResponseEntity.ok(this.membresiaService.listarTodas());
    }

    @PatchMapping({"/{idMembresia}/estado"})
    public ResponseEntity<Void> cambiarEstado(@PathVariable int idMembresia, @RequestParam EstadoMembresia estado) {
        this.membresiaService.cambiarEstado(idMembresia, estado);
        return ResponseEntity.noContent().build();
    }
}
