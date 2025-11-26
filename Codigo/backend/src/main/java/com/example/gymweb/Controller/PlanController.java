package com.example.gymweb.controller;

import com.example.gymweb.Service.PlanService;
import com.example.gymweb.dto.Request.PlanRequest;
import com.example.gymweb.dto.Response.PlanResponse;
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
@RequestMapping({"/api/planes"})
public class PlanController {
    private PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<PlanResponse> crear(@RequestBody PlanRequest request) {
        return ResponseEntity.ok(this.planService.crearPlan(request));
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> listar() {
        return ResponseEntity.ok(this.planService.listarPlanes());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<PlanResponse> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(this.planService.buscarPorId(id));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<PlanResponse> actualizar(@PathVariable int id, @RequestBody PlanRequest request) {
        return ResponseEntity.ok(this.planService.actualizarPlan(id, request));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        this.planService.eliminarPlan(id);
        return ResponseEntity.noContent().build();
    }
}