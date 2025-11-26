package com.example.gymweb.dto.Response;
import java.time.LocalDateTime;

public class MensajeClaseResponse {
    private int idMensaje;
    private String mensaje;
    private LocalDateTime fecha;

    public int getIdMensaje() {
        return this.idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}