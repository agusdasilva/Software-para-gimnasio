package com.example.gymweb.Controller;

import com.example.gymweb.Service.HorarioService;
import com.example.gymweb.dto.Request.HorarioUpdateRequest;
import com.example.gymweb.dto.Response.HorarioDiaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horario")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping
    public ResponseEntity<java.util.List<HorarioDiaResponse>> obtener() {
        return ResponseEntity.ok(horarioService.obtener());
    }

    @PutMapping
    public ResponseEntity<java.util.List<HorarioDiaResponse>> actualizar(@RequestBody HorarioUpdateRequest request) {
        return ResponseEntity.ok(horarioService.actualizar(request));
    }
}
