package com.example.gymweb.dto.Request;

public class MembresiaRequest {
    private int idUsuario;
    private int idPlan;

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPlan() {
        return this.idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }
}