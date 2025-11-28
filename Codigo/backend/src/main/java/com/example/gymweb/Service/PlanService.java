package com.example.gymweb.Service;

import com.example.gymweb.Repository.PlanRepository;
import com.example.gymweb.dto.Request.PlanRequest;
import com.example.gymweb.dto.Response.PlanResponse;
import com.example.gymweb.model.Plan;
import com.example.gymweb.Service.NotificacionService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private NotificacionService notificacionService;

    private PlanResponse convertirAResponse(Plan plan) {
        PlanResponse res = new PlanResponse();
        res.setId(plan.getId());
        res.setNombre(plan.getNombre());
        res.setPrecio(plan.getPrecio());
        res.setPeriodo(plan.getPeriodo());
        return res;
    }

    public PlanResponse crearPlan(PlanRequest request) {
        Plan plan = new Plan();
        plan.setNombre(request.getNombre());
        plan.setPrecio(request.getPrecio());
        plan.setPeriodo(request.getPeriodo());
        this.planRepository.save(plan);
        String mensaje = String.format("Nuevo plan disponible: %s - %s $%s", plan.getNombre(), plan.getPeriodo(), plan.getPrecio());
        this.notificacionService.crearParaTodos(mensaje);
        return this.convertirAResponse(plan);
    }

    public List<PlanResponse> listarPlanes() {
        return this.planRepository.findAll().stream().map(this::convertirAResponse).toList();
    }

    public PlanResponse buscarPorId(int id) {
        Plan plan = (Plan)this.planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        return this.convertirAResponse(plan);
    }

    public PlanResponse actualizarPlan(int id, PlanRequest request) {
        Plan plan = (Plan)this.planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        plan.setNombre(request.getNombre());
        plan.setPrecio(request.getPrecio());
        plan.setPeriodo(request.getPeriodo());
        this.planRepository.save(plan);
        return this.convertirAResponse(plan);
    }

    public void eliminarPlan(int id) {
        this.planRepository.deleteById(id);
    }
}

