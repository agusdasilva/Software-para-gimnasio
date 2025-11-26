package com.example.gymweb.Controller;


import com.example.gymweb.Service.EjercicioService;
import com.example.gymweb.dto.Request.EjercicioRequest;
import com.example.gymweb.dto.Response.EjercicioResponse;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/ejercicio"})
public class EjercicioController {
    private EjercicioService service;

    @Autowired
    public EjercicioController(EjercicioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EjercicioResponse> crearEjercicio(@RequestBody EjercicioRequest request) {
        return ResponseEntity.ok(this.service.crearEjercicio(request));
    }

    @GetMapping({"/id/{id}"})
    public ResponseEntity<EjercicioResponse> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(this.service.buscarPorId(id));
    }

    @GetMapping({"/nombre/{nombre}"})
    public ResponseEntity<EjercicioResponse> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(this.service.buscarPorNombre(nombre));
    }

    @GetMapping
    public ResponseEntity<List<EjercicioResponse>> listar() {
        return ResponseEntity.ok(this.service.listar());
    }

    @GetMapping({"/listarXnombre/{nombre}"})
    public ResponseEntity<List<EjercicioResponse>> listarPorContenerNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(this.service.listarPorContenerNombre(nombre));
    }

    @GetMapping({"/grupoMuscular/{grupo_muscular}"})
    public ResponseEntity<List<EjercicioResponse>> listarPorgrupo_muscular(@PathVariable String grupo_muscular) {
        return ResponseEntity.ok(this.service.listarPorgrupo_muscular(grupo_muscular));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<EjercicioResponse> eliminarPorId(@PathVariable int id) {
        this.service.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }
}