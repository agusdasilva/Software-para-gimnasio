package com.example.gymweb.dto.Response;

public class RutinaResponse {
    private int id;
    private String nombre;
    private String creador;
    private RutinaDetalleResponse detalle;
    private boolean esGlobal;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreador() {
        return this.creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public RutinaDetalleResponse getDetalle() {
        return this.detalle;
    }

    public void setDetalle(RutinaDetalleResponse detalle) {
        this.detalle = detalle;
    }

    public boolean isEsGlobal() {
        return this.esGlobal;
    }

    public void setEsGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
}

