package com.example.gymweb.Controller;


import com.example.gymweb.Service.EjercicioDetalleService;
import com.example.gymweb.dto.Request.EjercicioDetalleRequest;
import com.example.gymweb.dto.Request.SerieRequest;
import com.example.gymweb.dto.Response.EjercicioDetalleResponse;
import com.example.gymweb.dto.Response.SerieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/ejercicio-detalle"})
public class EjercicioDetalleController {
    private final EjercicioDetalleService service;

    @Autowired
    public EjercicioDetalleController(EjercicioDetalleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EjercicioDetalleResponse> crear(@RequestBody EjercicioDetalleRequest request) {
        return ResponseEntity.ok(this.service.crearEjercicioDetalle(request));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        this.service.eliminarEjercicioDetalle(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping({"/{idDetalle}/series"})
    public ResponseEntity<EjercicioDetalleResponse> agregarSerie(@PathVariable int idDetalle, @RequestBody SerieRequest request) {
        return ResponseEntity.ok(this.service.agregarSerie(idDetalle, request));
    }

    @PutMapping({"/serie/{idSerie}"})
    public ResponseEntity<SerieResponse> modificarSerie(@PathVariable int idSerie, @RequestBody SerieRequest request) {
        return ResponseEntity.ok(this.service.modificarSerie(idSerie, request));
    }
}
