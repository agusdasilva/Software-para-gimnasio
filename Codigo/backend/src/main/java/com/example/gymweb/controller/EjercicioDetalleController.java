package com.example.gymweb.controller;

import com.example.gymweb.dto.Request.EjercicioDetalleRequest;
import com.example.gymweb.dto.Request.SerieRequest;
import com.example.gymweb.dto.Response.EjercicioDetalleResponse;
import com.example.gymweb.dto.Response.SerieResponse;
import com.example.gymweb.service.EjercicioDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ejercicio-detalle")
public class EjercicioDetalleController {

    private final EjercicioDetalleService service;

    @Autowired
    public EjercicioDetalleController(EjercicioDetalleService service) {
        this.service = service;
    }

    // ---------------------------------------------------
    // Crear un ejercicio detalle con sus series
    // ---------------------------------------------------

    @PostMapping
    public ResponseEntity<EjercicioDetalleResponse> crear(@RequestBody EjercicioDetalleRequest request) {
        return ResponseEntity.ok(service.crearEjercicioDetalle(request));
    }

    // ---------------------------------------------------
    // Eliminar ejercicio detalle
    // ---------------------------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminarEjercicioDetalle(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------
    // Agregar una serie a un detalle
    // ---------------------------------------------------

    @PostMapping("/{idDetalle}/series")
    public ResponseEntity<EjercicioDetalleResponse> agregarSerie(
            @PathVariable int idDetalle,
            @RequestBody SerieRequest request
    ) {
        return ResponseEntity.ok(service.agregarSerie(idDetalle, request));
    }

    // ---------------------------------------------------
    // Modificar una serie
    // ---------------------------------------------------

    @PutMapping("/serie/{idSerie}")
    public ResponseEntity<SerieResponse> modificarSerie(
            @PathVariable int idSerie,
            @RequestBody SerieRequest request
    ) {
        return ResponseEntity.ok(service.modificarSerie(idSerie, request));
    }
}
