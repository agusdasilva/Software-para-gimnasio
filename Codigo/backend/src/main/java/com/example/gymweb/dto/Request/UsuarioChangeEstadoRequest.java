package com.example.gymweb.dto.Request;

import com.example.gymweb.model.Estado;

public class UsuarioChangeEstadoRequest {

    private Integer idUsuario;
    private Estado nuevoEstado;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Estado getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(Estado nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}
