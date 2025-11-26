package com.example.gymweb.dto.Request;

public class InvitacionRequest {
    private int idUsuario;

    public InvitacionRequest(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public InvitacionRequest() {
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
