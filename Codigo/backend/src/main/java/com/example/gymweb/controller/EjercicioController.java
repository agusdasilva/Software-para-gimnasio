package com.example.gymweb.controller;

import com.example.gymweb.dto.Request.EjercicioRequest;
import com.example.gymweb.dto.Response.EjercicioResponse;
import com.example.gymweb.service.EjercicioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicio")
public class EjercicioController {

    private EjercicioService service;

    @Autowired
    public EjercicioController(EjercicioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EjercicioResponse> crearEjercicio(@RequestBody EjercicioRequest request)
    {
        return ResponseEntity.ok(service.crearEjercicio(request));
    }

    @GetMapping ("/id/{id}")
    public ResponseEntity<EjercicioResponse> buscarPorId(@PathVariable int id)
    {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping ("/nombre/{nombre}")
    public ResponseEntity<EjercicioResponse> buscarPorNombre(@PathVariable String nombre)
    {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    @GetMapping
    public ResponseEntity<List<EjercicioResponse>> listar()
    {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping ("/listarXnombre/{nombre}")
    public ResponseEntity<List<EjercicioResponse>> listarPorContenerNombre(@PathVariable String nombre)
    {
        return ResponseEntity.ok(service.listarPorContenerNombre(nombre));
    }

    @GetMapping ("/grupoMuscular/{grupo_muscular}")
    public ResponseEntity<List<EjercicioResponse>> listarPorgrupo_muscular(@PathVariable String grupo_muscular)
    {
        return ResponseEntity.ok(service.listarPorgrupo_muscular(grupo_muscular));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<EjercicioResponse> eliminarPorId(@PathVariable int id)
    {
        service.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }



}
