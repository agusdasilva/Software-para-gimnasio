package com.example.gymweb.dto.Response;

import java.time.LocalDateTime;

public class InvitacionResponse {
    private int idInvitacion;
    private String estado;
    private LocalDateTime fecha;
    private int IdUsuario;
    private int IdClase;

    public InvitacionResponse() {
    }

    public InvitacionResponse(int idInvitacion, String estado, LocalDateTime fecha, int idUsuario, int idClase) {
        this.idInvitacion = idInvitacion;
        this.estado = estado;
        this.fecha = fecha;
        this.IdUsuario = idUsuario;
        this.IdClase = idClase;
    }

    public int getIdInvitacion() {
        return this.idInvitacion;
    }

    public String getEstado() {
        return this.estado;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setIdInvitacion(int idInvitacion) {
        this.idInvitacion = idInvitacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdUsuario() {
        return this.IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.IdUsuario = idUsuario;
    }

    public int getIdClase() {
        return this.IdClase;
    }

    public void setIdClase(int idClase) {
        this.IdClase = idClase;
    }
}
