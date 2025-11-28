package com.example.gymweb.Controller;

import com.example.gymweb.Service.DashboardConfigService;
import com.example.gymweb.dto.Request.DashboardConfigUpdateRequest;
import com.example.gymweb.dto.Response.DashboardConfigResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard-config")
public class DashboardConfigController {

    private final DashboardConfigService service;

    public DashboardConfigController(DashboardConfigService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DashboardConfigResponse> obtener() {
        return ResponseEntity.ok(service.obtener());
    }

    @PutMapping
    public ResponseEntity<DashboardConfigResponse> actualizar(@RequestBody DashboardConfigUpdateRequest request) {
        return ResponseEntity.ok(service.actualizar(request));
    }
}
