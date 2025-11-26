package com.example.gymweb.Service;

import com.example.gymweb.Repository.PreferenciaNotificacionRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Response.PreferenciaResponse;
import com.example.gymweb.model.PreferenciaNotificacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreferenciaService {
    @Autowired
    private PreferenciaNotificacionRepository repo;
    @Autowired
    private UsuarioRepository usuarioRepo;

    public PreferenciaService(PreferenciaNotificacionRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    public PreferenciaResponse obtener(int idUsuario) {
        PreferenciaNotificacion p = (PreferenciaNotificacion)this.repo.findByUsuario_Id(idUsuario).orElseThrow(() -> new RuntimeException("No tiene preferencia cargada"));
        PreferenciaResponse response = new PreferenciaResponse();
        response.setSilencioso(p.getSilencioso());
        return response;
    }

    public PreferenciaResponse actualizar(int idUsuario, boolean silencioso) {
        PreferenciaNotificacion p = (PreferenciaNotificacion)this.repo.findByUsuario_Id(idUsuario).orElseThrow(() -> new RuntimeException("Preferencia no encontrada"));
        p.setSilencioso(silencioso);
        this.repo.save(p);
        PreferenciaResponse response = new PreferenciaResponse();
        response.setSilencioso(p.getSilencioso());
        return response;
    }
}