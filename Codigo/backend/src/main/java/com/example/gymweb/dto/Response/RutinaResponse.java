package com.example.gymweb.dto.Response;

public class RutinaResponse {
    private int id;
    private String nombre;
    private String creador;
    private RutinaDetalleResponse detalle;
    private boolean esGlobal;

    public int getId() {
<<<<<<< Updated upstream
        return id;
=======
        return this.id;
>>>>>>> Stashed changes
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
<<<<<<< Updated upstream
        return nombre;
=======
        return this.nombre;
>>>>>>> Stashed changes
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreador() {
<<<<<<< Updated upstream
        return creador;
=======
        return this.creador;
>>>>>>> Stashed changes
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public RutinaDetalleResponse getDetalle() {
<<<<<<< Updated upstream
        return detalle;
=======
        return this.detalle;
>>>>>>> Stashed changes
    }

    public void setDetalle(RutinaDetalleResponse detalle) {
        this.detalle = detalle;
    }

    public boolean isEsGlobal() {
<<<<<<< Updated upstream
        return esGlobal;
=======
        return this.esGlobal;
>>>>>>> Stashed changes
    }

    public void setEsGlobal(boolean esGlobal) {
        this.esGlobal = esGlobal;
    }
}
