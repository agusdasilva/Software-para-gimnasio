package com.example.gymweb.controller;


import com.example.gymweb.Service.InvitacionService;
import com.example.gymweb.dto.Response.InvitacionResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/invitaciones"})
public class InvitacionController {
    private InvitacionService invitacionService;

    @Autowired
    public InvitacionController(InvitacionService invitacionService) {
        this.invitacionService = invitacionService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<InvitacionResponse> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(this.invitacionService.buscarPorId(id));
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public ResponseEntity<List<InvitacionResponse>> listarPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(this.invitacionService.listarPorUsuario(idUsuario));
    }
}
