package com.example.gymweb.dto.Response;

public class RutinaResponse {
    private int id;
    private String nombre;
    private String creador;
    private RutinaDetalleResponse detalle;
    private boolean esGlobal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public RutinaDetalleResponse getDetalle() {
        return detalle;
    }

    public void setDetalle(RutinaDetalleResponse detalle) {
        this.detalle = detalle;
    }

    public boolean isEsGlobal() {
        return esGlobal;
    }

    public void setEsGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
}
