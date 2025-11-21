package com.example.gymweb.controller;

import com.example.gymweb.dto.Request.RutinaRequest;
import com.example.gymweb.dto.Response.RutinaResponse;
import com.example.gymweb.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutina")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    // ----------------------- CREAR RUTINA + DETALLE -----------------------
    @PostMapping
    public ResponseEntity<RutinaResponse> crearRutina(@RequestBody RutinaRequest request) {
        return ResponseEntity.ok(rutinaService.crearRutina(request));
    }

    // ----------------------- BUSCAR POR ID -----------------------
    @GetMapping("/id/{id}")
    public ResponseEntity<RutinaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(rutinaService.buscarPorId(id));
    }

    // ----------------------- BUSCAR POR NOMBRE -----------------------
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RutinaResponse> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(rutinaService.buscarPorNombre(nombre));
    }

    // ----------------------- MODIFICAR DESCANSO -----------------------
    @PutMapping("/{idRutina}/modificar-descanso/{nuevoDescanso}")
    public ResponseEntity<RutinaResponse> modificarDescanso(
            @PathVariable Integer idRutina,
            @PathVariable int nuevoDescanso
    ) {
        return ResponseEntity.ok(rutinaService.modificarDescanso(idRutina, nuevoDescanso));
    }

    // ----------------------- ELIMINAR -----------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable Integer id) {
        rutinaService.elimninarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------- LISTAR TODAS -----------------------
    @GetMapping
    public ResponseEntity<List<RutinaResponse>> listarTodas() {
        return ResponseEntity.ok(rutinaService.listarTodas());
    }
}