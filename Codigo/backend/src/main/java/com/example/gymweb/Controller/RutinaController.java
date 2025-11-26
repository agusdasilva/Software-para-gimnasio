package com.example.gymweb.Controller;

import com.example.gymweb.Service.RutinaService;
import com.example.gymweb.dto.Request.RutinaRequest;
import com.example.gymweb.dto.Response.RutinaResponse;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/rutina"})
public class RutinaController {
    private RutinaService rutinaService;

    @Autowired
    public RutinaController(RutinaService rutinaService) {
        this.rutinaService = rutinaService;
    }

    @PostMapping
    public ResponseEntity<RutinaResponse> crearRutina(@RequestBody RutinaRequest request) {
        return ResponseEntity.ok(this.rutinaService.crearRutina(request));
    }

    @GetMapping({"/id/{id}"})
    public ResponseEntity<RutinaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.rutinaService.buscarPorId(id));
    }

    @GetMapping({"/nombre/{nombre}"})
    public ResponseEntity<RutinaResponse> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(this.rutinaService.buscarPorNombre(nombre));
    }

    @PutMapping({"/{idRutina}/modificar-descanso/{nuevoDescanso}"})
    public ResponseEntity<RutinaResponse> modificarDescanso(@PathVariable Integer idRutina, @PathVariable int nuevoDescanso) {
        return ResponseEntity.ok(this.rutinaService.modificarDescanso(idRutina, nuevoDescanso));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> eliminarRutina(@PathVariable Integer id) {
        this.rutinaService.elimninarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RutinaResponse>> listarTodas() {
        return ResponseEntity.ok(this.rutinaService.listarTodas());
    }
}
