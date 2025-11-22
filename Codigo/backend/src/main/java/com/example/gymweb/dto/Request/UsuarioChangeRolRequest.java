package com.example.gymweb.dto.Request;

import com.example.gymweb.model.Rol;

public class UsuarioChangeRolRequest {

    private Integer idUsuario;
    private Rol nuevoRol;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Rol getNuevoRol() {
        return nuevoRol;
    }

    public void setNuevoRol(Rol nuevoRol) {
        this.nuevoRol = nuevoRol;
    }
}